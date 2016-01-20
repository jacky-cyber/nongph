package cn.globalph.b2c.order.service;

import cn.globalph.b2c.catalog.domain.CategoryProductXref;
import cn.globalph.b2c.delivery.DeliveryType;
import cn.globalph.b2c.offer.dao.OfferDao;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.order.dao.OrderDao;
import cn.globalph.b2c.order.dao.OrderItemDao;
import cn.globalph.b2c.order.domain.NullOrderFactory;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.exception.*;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.payment.dao.OrderPaymentDao;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.b2c.payment.service.SecureOrderPaymentService;
import cn.globalph.b2c.pricing.service.PricingService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.workflow.ActivityMessages;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.Processor;
import cn.globalph.b2c.workflow.WorkflowException;
import cn.globalph.common.email.service.EmailService;
import cn.globalph.common.email.service.info.EmailInfo;
import cn.globalph.common.email.service.message.Attachment;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.util.TableCreator;
import cn.globalph.common.util.TransactionUtils;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.apply.condition.impl.CategoryApplyCondition;
import cn.globalph.coupon.apply.condition.impl.ProductApplyCondition;
import cn.globalph.coupon.apply.condition.impl.ProviderApplyCondition;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("blOrderService")
@ManagedResource(objectName = "org.broadleafcommerce:name=OrderService", description = "Order Service", currencyTimeLimit = 15)
public class OrderServiceImpl implements OrderService {
    private static final Log LOG = LogFactory.getLog(OrderServiceImpl.class);

    /* DAOs */
    @Resource(name = "blOrderPaymentDao")
    protected OrderPaymentDao paymentDao;

    @Resource(name = "blOrderDao")
    protected OrderDao orderDao;

	@Resource(name = "blOrderItemDao")
	protected OrderItemDao orderItemDao;
	
	@Resource(name = "blOfferDao")
	protected OfferDao offerDao;

    /* Factories */
    @Resource(name = "blNullOrderFactory")
    protected NullOrderFactory nullOrderFactory;

    /* Services */
    @Resource(name = "blPricingService")
    protected PricingService pricingService;

    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "blOfferService")
    protected OfferService offerService;

    @Resource(name = "blSecureOrderPaymentService")
    protected SecureOrderPaymentService securePaymentInfoService;

    @Resource(name = "blMergeCartService")
    protected MergeCartService mergeCartService;

    @Resource(name = "blOrderServiceExtensionManager")
    protected OrderServiceExtensionManager extensionManager;

    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    /* work flows */
    @Resource(name = "blAddItemWorkflow")
    protected Processor addItemWorkflow;

    @Resource(name = "blUpdateProductOptionsForItemWorkflow")
    private Processor updateProductOptionsForItemWorkflow;

    @Resource(name = "blUpdateItemWorkflow")
    protected Processor updateItemWorkflow;

    @Resource(name = "blRemoveItemWorkflow")
    protected Processor removeItemWorkflow;

    @Resource(name = "blTransactionManager")
    protected PlatformTransactionManager transactionManager;
    
    @Resource(name="phSendDeliveryInfoEmailInfo")
    protected EmailInfo sendDeliveryInfoEmailInfo;
    
    @Resource(name="blEmailService")
    protected EmailService emailService;   
    
    @Resource(name = "blSkuDao")
    protected SkuDao skuDao;
    
    @Value("${pricing.retry.count.for.lock.failure}")
    protected int pricingRetryCountForLockFailure = 3;

    @Value("${pricing.retry.wait.interval.for.lock.failure}")
    protected long pricingRetryWaitIntervalForLockFailure = 500L;

    /* Fields */
    protected boolean moveNamedOrderItems = true;
    protected boolean deleteEmptyNamedOrders = true;

    @Value("${automatically.merge.like.items}")
    protected boolean automaticallyMergeLikeItems;
    
    @Override
    @ManagedAttribute(description = "The delete empty named order after adding items to cart attribute", currencyTimeLimit = 15)
    public void setDeleteEmptyNamedOrders(boolean deleteEmptyNamedOrders) {
        this.deleteEmptyNamedOrders = deleteEmptyNamedOrders;
    }

    @Override
    public boolean getAutomaticallyMergeLikeItems() {
        return automaticallyMergeLikeItems;
    }

    @Override
    public void setAutomaticallyMergeLikeItems(
            boolean automaticallyMergeLikeItems) {
        this.automaticallyMergeLikeItems = automaticallyMergeLikeItems;
    }

    @Override
    @ManagedAttribute(description = "The move item from named order when adding to the cart attribute", currencyTimeLimit = 15)
    public boolean isMoveNamedOrderItems() {
        return moveNamedOrderItems;
    }

    @Override
    @ManagedAttribute(description = "The move item from named order when adding to the cart attribute", currencyTimeLimit = 15)
    public void setMoveNamedOrderItems(boolean moveNamedOrderItems) {
        this.moveNamedOrderItems = moveNamedOrderItems;
    }

    @Override
    @ManagedAttribute(description = "The delete empty named order after adding items to cart attribute", currencyTimeLimit = 15)
    public boolean isDeleteEmptyNamedOrders() {
        return deleteEmptyNamedOrders;
    }

    @Override
    public Order findNamedOrderForCustomer(String name, Customer customer) {
        return orderDao.readNamedOrderForCustomer(customer, name);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderDao.get(orderId);
    }

    @Override
    public Order getOrderById(Long orderId, boolean refresh) {
        return orderDao.readOrderById(orderId, refresh);
    }

    @Override
    public Order getNullOrder() {
        return nullOrderFactory.getNullOrder();
    }

    @Override
    public Order findCartForCustomer(Customer customer) {
        return orderDao.readCartForCustomer(customer);
    }

    @Override
    public List<Order> findOrdersForCustomer(Customer customer) {
        return orderDao.readOrdersForCustomer(customer.getId());
    }

    @Override
    public List<Order> findOrdersForCustomer(Customer customer,
                                             OrderStatus status) {
        return orderDao.readOrdersForCustomer(customer, status);
    }

    @Override
    public List<Order> findOrdersForCustomer(Customer customer,
                                             List<OrderStatus> sts) {
        return orderDao.readOrdersForCustomer(customer, sts);
    }
    
    public Integer getOrdersForCustomerTotal(Customer customer, List<OrderStatus> sts){
    	return orderDao.getOrdersForCustomerTotal(customer, sts);
    }
    
    public List<Order> findOrdersForCustomer(Customer customer, List<OrderStatus> sts, int pageSize, int page){
    	return orderDao.readOrdersForCustomer(customer, sts, pageSize, page);
    }

    @Override
    @Transactional("blTransactionManager")
    public Order findOrderByOrderNumber(String orderNumber) {
        return orderDao.readOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<OrderPayment> findPaymentsForOrder(Order order) {
        return paymentDao.readPaymentsForOrder(order);
    }

    @Override
    public OrderItem findLastMatchingItem(Order order, Long skuId,
                                          Long productId) {
        if (order.getOrderItems() != null) {
            for (int i = (order.getOrderItems().size() - 1); i >= 0; i--) {
                OrderItem currentItem = (order.getOrderItems().get(i));
                if (skuId != null) {
                    if (currentItem.getSku() != null
                            && skuId.equals(currentItem.getSku().getId())) {
                        return currentItem;
                    }
                } else if (productId != null
                        && productId.equals(currentItem.getSku().getProduct()
                        .getId())) {
                    return currentItem;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional("blTransactionManager")
    public Order createNewCartForCustomer(Customer customer) {
        if (!customer.isPersisted())
            customerService.saveCustomer(customer);
        return orderDao.createNewCartForCustomer(customer);
    }

    @Override
    @Transactional("blTransactionManager")
    public Order createNamedOrderForCustomer(String name, Customer customer) {
        Order namedOrder = orderDao.create();
        namedOrder.setCustomer(customer);
        namedOrder.setName(name);
        namedOrder.setStatus(OrderStatus.NAMED);

        if (extensionManager != null) {
            extensionManager.getHandlerProxy()
                    .attachAdditionalDataToNewNamedCart(customer, namedOrder);
        }

        return orderDao.persist(namedOrder); // No need to price here
    }

    @Override
    public Order createSuborderForOrder(Order order) {
        Order suborder = orderDao.create();
        suborder.setOrder(order);
        suborder.setCustomer(order.getCustomer());
        suborder.setStatus(OrderStatus.SUBORDER);
        return suborder;
    }

    @Override
    public List<Order> readSuborderList(Long orderId) {
        return orderDao.readSuborderList(orderId);
    }

    @Override
    @Transactional("blTransactionManager")
    public OrderPayment addPaymentToOrder(Order order, OrderPayment payment,
                                          Referenced securePaymentInfo) {
        payment.setOrder(order);
        order.getPayments().add(payment);
        order = persistOrder(order);
        int paymentIndex = order.getPayments().size() - 1;

		if (securePaymentInfo != null) {
			securePaymentInfoService.save(securePaymentInfo);
		}

		return order.getPayments().get(paymentIndex);
	}

	@Override
	@Transactional("blTransactionManager")
	public Order save(Order order, Boolean priceOrder) throws PricingException {
		// persist the order first
		TransactionStatus status = TransactionUtils.createTransaction(
				"saveOrder", TransactionDefinition.PROPAGATION_REQUIRED,
				transactionManager);
		try {
            order = save(order);
            TransactionUtils.finalizeTransaction(status, transactionManager,
					false);
		} catch (RuntimeException ex) {
			TransactionUtils.finalizeTransaction(status, transactionManager,
					true);
			throw ex;
		}

		// make any pricing changes - possibly retrying with the persisted state
		// if there's a lock failure
		if (priceOrder) {
			int retryCount = 0;
			boolean isValid = false;
			while (!isValid) {
				try {
					order = pricingService.executePricing(order);
					isValid = true;
				} catch (Exception ex) {
					boolean isValidCause = false;
					Throwable cause = ex;
					while (!isValidCause) {
						if (cause.getClass().equals(
								LockAcquisitionException.class)) {
							isValidCause = true;
						}
						cause = cause.getCause();
						if (cause == null) {
							break;
						}
					}
					if (isValidCause) {
						if (LOG.isInfoEnabled()) {
							LOG.info("Problem acquiring lock during pricing call - attempting to price again.");
						}
						isValid = false;
						if (retryCount >= pricingRetryCountForLockFailure) {
							if (LOG.isInfoEnabled()) {
								LOG.info("Problem acquiring lock during pricing call. Retry limit exceeded at ("
										+ retryCount + "). Throwing exception.");
							}
							if (ex instanceof PricingException) {
								throw (PricingException) ex;
							} else {
								throw new PricingException(ex);
							}
						} else {
							order = getOrderById(order.getId());
							retryCount++;
						}
						try {
							Thread.sleep(pricingRetryWaitIntervalForLockFailure);
						} catch (Throwable e) {
							// do nothing
						}
					} else {
						if (ex instanceof PricingException) {
							throw (PricingException) ex;
						} else {
							throw new PricingException(ex);
						}
					}
				}
			}

			// make the final save of the priced order
			status = TransactionUtils.createTransaction("saveOrder",
					TransactionDefinition.PROPAGATION_REQUIRED,
					transactionManager);
			try {
				order = persistOrder(order);
				TransactionUtils.finalizeTransaction(status,
						transactionManager, false);
			} catch (RuntimeException ex) {
				TransactionUtils.finalizeTransaction(status,
						transactionManager, true);
				throw ex;
			}
		}

		return order;
	}

	// This method exists to provide OrderService methods the ability to save an
	// order
	// without having to worry about a PricingException being thrown.
    private Order persistOrder(Order order) {
        return orderDao.persist(order);
    }

    private Order save(Order order) {
        return orderDao.save(order);
    }

	@Override
	@Transactional("blTransactionManager")
	public void cancelOrder(Order order) {
		orderDao.delete(order);
	}

	@Override
	@Transactional("blTransactionManager")
	public void deleteOrder(Order order) {
		orderDao.delete(order);
	}

	@Override
	@Transactional("blTransactionManager")
	public Order confirmOrder(Order order) {
		return orderDao.submitOrder(order);
	}

	@Override
	@Transactional("blTransactionManager")
	public Order addAllItemsFromNamedOrder(Order namedOrder, boolean priceOrder)
			throws RemoveFromCartException, AddToCartException {
		Order cartOrder = orderDao
				.readCartForCustomer(namedOrder.getCustomer());
		if (cartOrder == null) {
			cartOrder = createNewCartForCustomer(namedOrder.getCustomer());
		}
		List<OrderItem> items = new ArrayList<OrderItem>(
				namedOrder.getOrderItems());
		for (OrderItem item : items) {
			if (moveNamedOrderItems) {
				removeItem(namedOrder.getId(), item.getId(), false);
			}

			OrderItemRequestDTO orderItemRequest = orderItemService
					.buildOrderItemRequestDTOFromOrderItem(item);
			cartOrder = addItem(cartOrder.getId(), orderItemRequest, priceOrder);
		}

		if (deleteEmptyNamedOrders) {
			cancelOrder(namedOrder);
		}

		return cartOrder;
	}

	@Override
	@Transactional("blTransactionManager")
	public Order addItemFromNamedOrder(Order namedOrder, OrderItem item,
			boolean priceOrder) throws RemoveFromCartException,
			AddToCartException {
		Order cartOrder = orderDao
				.readCartForCustomer(namedOrder.getCustomer());
		if (cartOrder == null) {
			cartOrder = createNewCartForCustomer(namedOrder.getCustomer());
		}

		if (moveNamedOrderItems) {
			removeItem(namedOrder.getId(), item.getId(), false);
		}

		OrderItemRequestDTO orderItemRequest = orderItemService
				.buildOrderItemRequestDTOFromOrderItem(item);
		cartOrder = addItem(cartOrder.getId(), orderItemRequest, priceOrder);

		if (namedOrder.getOrderItems().size() == 0 && deleteEmptyNamedOrders) {
			cancelOrder(namedOrder);
		}

		return cartOrder;
	}

	@Override
	@Transactional("blTransactionManager")
	public Order addItemFromNamedOrder(Order namedOrder, OrderItem item,
			int quantity, boolean priceOrder) throws RemoveFromCartException,
			AddToCartException, UpdateCartException {
		// Validate that the quantity requested makes sense
		if (quantity < 1 || quantity > item.getQuantity()) {
			throw new IllegalArgumentException("Cannot move 0 or less quantity");
		} else if (quantity == item.getQuantity()) {
			return addItemFromNamedOrder(namedOrder, item, priceOrder);
		}

		Order cartOrder = orderDao
				.readCartForCustomer(namedOrder.getCustomer());
		if (cartOrder == null) {
			cartOrder = createNewCartForCustomer(namedOrder.getCustomer());
		}

		if (moveNamedOrderItems) {
			// Update the old item to its new quantity only if we're moving
			// items
			OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
			orderItemRequestDTO.setOrderItemId(item.getId());
			orderItemRequestDTO.setQuantity(item.getQuantity() - quantity);
			updateItemQuantity(namedOrder.getId(), orderItemRequestDTO, false);
		}

		OrderItemRequestDTO orderItemRequest = orderItemService
				.buildOrderItemRequestDTOFromOrderItem(item);
		orderItemRequest.setQuantity(quantity);
		cartOrder = addItem(cartOrder.getId(), orderItemRequest, priceOrder);

		return cartOrder;
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { AddToCartException.class })
	public Order addItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO,
			boolean priceOrder) throws AddToCartException {
		// Don't allow overrides from this method.
		orderItemRequestDTO.setOverrideRetailPrice(null);
		orderItemRequestDTO.setOverrideSalePrice(null);
		return addItemWithPriceOverrides(orderId, orderItemRequestDTO,
				priceOrder);
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { AddToCartException.class })
	public Order addItemWithPriceOverrides(Long orderId,
			OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
			throws AddToCartException {
		Order order = getOrderById(orderId);
		preValidateCartOperation(order);
		if (automaticallyMergeLikeItems) {
			OrderItem item = findMatchingItem(order, orderItemRequestDTO);
			if (item != null && !item.isUsed()) {
				order.getOrderItems().remove(item);
				orderItemRequestDTO.setQuantity(item.getQuantity());
				orderItemService.delete(item);
				item = null;
				orderItemRequestDTO.setOrderItemId(null);
			}
			if (item != null) {
				orderItemService.saveOrderItem(item);
				orderItemRequestDTO.setQuantity(item.getQuantity()
						+ orderItemRequestDTO.getQuantity());
				orderItemRequestDTO.setOrderItemId(item.getId());
				try {
					return updateItemQuantity(orderId, orderItemRequestDTO,
							priceOrder);
				} catch (RemoveFromCartException e) {
					throw new AddToCartException(
							"Unexpected error - system tried to remove item while adding to cart",
							e);
				} catch (UpdateCartException e) {
					throw new AddToCartException(
							"Could not update quantity for matched item", e);
				}
			}
		}
		try {
			// We only want to price on the last addition for performance
			// reasons and only if the user asked for it.
			int numAdditionRequests = priceOrder ? 1 : -1;
			int currentAddition = 1;

			CartOperationRequest cartOpRequest = new CartOperationRequest(
					order, orderItemRequestDTO,
					currentAddition == numAdditionRequests);
			ProcessContext<CartOperationRequest> context = (ProcessContext<CartOperationRequest>) addItemWorkflow
					.doActivities(cartOpRequest);

			List<ActivityMessageDTO> orderMessages = new ArrayList<ActivityMessageDTO>();
			orderMessages.addAll(((ActivityMessages) context)
					.getActivityMessages());

			context.getSeedData().getOrder().setOrderMessages(orderMessages);
			return context.getSeedData().getOrder();
		} catch (WorkflowException e) {
			throw new AddToCartException("Could not add to cart",
					getCartOperationExceptionRootCause(e));
		}
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = {
			UpdateCartException.class, RemoveFromCartException.class })
	public Order updateItemQuantity(Long orderId,
			OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
			throws UpdateCartException, RemoveFromCartException {
		preValidateCartOperation(getOrderById(orderId));
		if (orderItemRequestDTO.getQuantity() == 0) {
			return removeItem(orderId, orderItemRequestDTO.getOrderItemId(),
					priceOrder);
		}

		try {
			CartOperationRequest cartOpRequest = new CartOperationRequest(
					getOrderById(orderId), orderItemRequestDTO, priceOrder);
			ProcessContext<CartOperationRequest> context = (ProcessContext<CartOperationRequest>) updateItemWorkflow
					.doActivities(cartOpRequest);
			context.getSeedData().getOrder().getOrderMessages()
					.addAll(((ActivityMessages) context).getActivityMessages());
			return context.getSeedData().getOrder();
		} catch (WorkflowException e) {
			throw new UpdateCartException("Could not update cart quantity",
					getCartOperationExceptionRootCause(e));
		}
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { RemoveFromCartException.class })
	public Order removeItem(Long orderId, Long orderItemId, boolean priceOrder)
			throws RemoveFromCartException {
		preValidateCartOperation(getOrderById(orderId));
		try {
			return removeItemInternal(orderId, orderItemId, priceOrder);
		} catch (WorkflowException e) {
			throw new RemoveFromCartException("Could not remove from cart",
					getCartOperationExceptionRootCause(e));
		}
	}

	private Order removeItemInternal(Long orderId, Long orderItemId,
			boolean priceOrder) throws WorkflowException {
		OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
		orderItemRequestDTO.setOrderItemId(orderItemId);
		CartOperationRequest cartOpRequest = new CartOperationRequest(
				getOrderById(orderId), orderItemRequestDTO, priceOrder);
		ProcessContext<CartOperationRequest> context = (ProcessContext<CartOperationRequest>) removeItemWorkflow
				.doActivities(cartOpRequest);
		context.getSeedData().getOrder().getOrderMessages()
				.addAll(((ActivityMessages) context).getActivityMessages());
		return context.getSeedData().getOrder();
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { UnuseFromCartException.class })
	public Order unuseItem(Long orderId, Long orderItemId, boolean priceOrder,
			boolean realDelete) throws UnuseFromCartException {
		preValidateCartOperation(getOrderById(orderId));
		try {
			return unuseItemInternal(orderId, orderItemId, priceOrder,
					realDelete);
		} catch (WorkflowException e) {
			throw new UnuseFromCartException("Could not unuse from cart",
					getCartOperationExceptionRootCause(e));
		}
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { RemoveFromCartException.class })
	public Order removeInactiveItems(Long orderId, boolean priceOrder)
			throws RemoveFromCartException {
		Order order = getOrderById(orderId);
		try {

			for (OrderItem currentItem : new ArrayList<OrderItem>(
					order.getOrderItems())) {
				if (!currentItem.isSkuActive()) {
					removeItem(orderId, currentItem.getId(), priceOrder);
				}
			}

		} catch (Exception e) {
			throw new RemoveFromCartException("Could not remove from cart",
					e.getCause());
		}
		return getOrderById(orderId);
	}

	private Order unuseItemInternal(Long orderId, Long orderItemId,
			boolean priceOrder, boolean realDelete) throws WorkflowException {
		OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
		orderItemRequestDTO.setOrderItemId(orderItemId);
		CartOperationRequest cartOpRequest = new CartOperationRequest(
				getOrderById(orderId), orderItemRequestDTO, priceOrder,
				realDelete);
		ProcessContext<CartOperationRequest> context = (ProcessContext<CartOperationRequest>) removeItemWorkflow
				.doActivities(cartOpRequest);
		context.getSeedData().getOrder().getOrderMessages()
				.addAll(((ActivityMessages) context).getActivityMessages());
		return context.getSeedData().getOrder();
	}

	@Override
	@Transactional("blTransactionManager")
	public void removeAllPaymentsFromOrder(Order order) {
		removePaymentsFromOrder(order, null);
	}

	@Override
	@Transactional("blTransactionManager")
	public void removePaymentsFromOrder(Order order, PaymentType paymentInfoType) {
		List<OrderPayment> infos = new ArrayList<OrderPayment>();
		for (OrderPayment paymentInfo : order.getPayments()) {
			if (paymentInfoType == null
					|| paymentInfoType.equals(paymentInfo.getType())) {
				infos.add(paymentInfo);
			}
		}
		order.getPayments().removeAll(infos);
		for (OrderPayment paymentInfo : infos) {
			try {
				securePaymentInfoService
						.findAndRemoveSecurePaymentInfo(
								paymentInfo.getReferenceNumber(),
								paymentInfo.getType());
			} catch (WorkflowException e) {
				// do nothing--this is an acceptable condition
				LOG.debug(
						"No secure payment is associated with the OrderPayment",
						e);
			}
			order.getPayments().remove(paymentInfo);
			paymentInfo = paymentDao.readPaymentById(paymentInfo.getId());
			paymentDao.delete(paymentInfo);
		}
	}

	@Override
	@Transactional("blTransactionManager")
	public void removePaymentFromOrder(Order order, OrderPayment payment) {
		OrderPayment paymentToRemove = null;
		for (OrderPayment info : order.getPayments()) {
			if (info.equals(payment)) {
				paymentToRemove = info;
			}
		}
		if (paymentToRemove != null) {
			try {
				securePaymentInfoService
						.findAndRemoveSecurePaymentInfo(
								paymentToRemove.getReferenceNumber(),
								payment.getType());
			} catch (WorkflowException e) {
				// do nothing--this is an acceptable condition
				LOG.debug(
						"No secure payment is associated with the OrderPayment",
						e);
			}
			order.getPayments().remove(paymentToRemove);
			payment = paymentDao.readPaymentById(paymentToRemove.getId());
			paymentDao.delete(payment);
		}
	}

	/**
	 * This method will return the exception that is immediately below the
	 * deepest WorkflowException in the current stack trace.
     *
     * @param e
	 *            the workflow exception that contains the requested root cause
	 * @return the root cause of the workflow exception
	 */
	private Throwable getCartOperationExceptionRootCause(WorkflowException e) {
		Throwable cause = e.getCause();
		if (cause == null) {
			return e;
		}

		Throwable currentCause = cause;
		while (currentCause.getCause() != null) {
			currentCause = currentCause.getCause();
			if (currentCause instanceof WorkflowException) {
				cause = currentCause.getCause();
			}
		}

		return cause;
	}

	private OrderItem findMatchingItem(Order order,
			OrderItemRequestDTO itemToFind) {
		if (order == null) {
			return null;
		}
		for (OrderItem currentItem : order.getOrderItems()) {
			if (itemMatches(currentItem.getSku(), itemToFind)) {
				return currentItem;
			}
		}
		return null;
	}

	private boolean itemMatches(Sku item1Sku, OrderItemRequestDTO item2) {
		// Must match on SKU and options
		if (item1Sku != null && item2.getSkuId() != null) {
			if (item1Sku.getId().equals(item2.getSkuId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(value = "blTransactionManager", rollbackFor = { UpdateCartException.class })
	public Order updateProductOptionsForItem(Long orderId,
			OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
			throws UpdateCartException {
		try {
			CartOperationRequest cartOpRequest = new CartOperationRequest(
					getOrderById(orderId), orderItemRequestDTO, priceOrder);
			ProcessContext<CartOperationRequest> context = (ProcessContext<CartOperationRequest>) updateProductOptionsForItemWorkflow
					.doActivities(cartOpRequest);
			context.getSeedData().getOrder().getOrderMessages()
					.addAll(((ActivityMessages) context).getActivityMessages());
			return context.getSeedData().getOrder();
		} catch (WorkflowException e) {
			throw new UpdateCartException("Could not product options",
					getCartOperationExceptionRootCause(e));
		}
	}

	@Override
	public void printOrder(Order order, Log log) {
		if (!log.isDebugEnabled()) {
			return;
		}

		TableCreator tc = new TableCreator(new TableCreator.Col[] {
				new TableCreator.Col("Order Item", 30),
				new TableCreator.Col("Qty"),
				new TableCreator.Col("Unit Price"),
				new TableCreator.Col("Avg Adj"),
				new TableCreator.Col("Total Adj"),
				new TableCreator.Col("Total Price") });

		for (OrderItem oi : order.getOrderItems()) {
			tc.addRow(new String[] { oi.getName(),
					String.valueOf(oi.getQuantity()),
					String.valueOf(oi.getPriceBeforeAdjustments(true)),
					String.valueOf(oi.getAverageAdjustmentValue()),
					String.valueOf(oi.getTotalAdjustmentValue()),
					String.valueOf(oi.getTotalPrice()) });
		}

		tc.addSeparator().withGlobalRowHeaderWidth(15)
				.addRow("Subtotal", order.getSubTotal())
				.addRow("Order Adj.", order.getOrderAdjustmentsValue())
				.addRow("Shipping", order.getTotalFulfillmentCharges())
				.addRow("Total", order.getTotal()).addSeparator();

		log.debug(tc.toString());
	}

	@Override
	public void preValidateCartOperation(Order cart) {
		ExtensionResultHolder erh = new ExtensionResultHolder();
		extensionManager.getHandlerProxy().preValidateCartOperation(cart, erh);
		if (erh.getThrowable() instanceof IllegalCartOperationException) {
			throw ((IllegalCartOperationException) erh.getThrowable());
		} else if (erh.getThrowable() != null) {
			throw new RuntimeException(erh.getThrowable());
		}
	}

	@Override
	public List<Order> findNoCommentOrdersForCustomer(Customer customer) {
		return orderDao.readNoCommentOrdersForCustomer(customer);
	}

	@Override
    public List<Order> findNoCommentOrdersForCustomer(Customer customer, int pageSize, int page){
		return orderDao.readNoCommentOrdersForCustomer(customer, pageSize, page);
	}
    
	@Override
    public Integer findNoCommentOrdersForCustomerTotal(Customer customer){
		return orderDao.readNoCommentOrdersForCustomerTotal(customer);
	}
//	private void putPrividerOrder(String orderNumber,
//			Map<String, List<String>> providerOrders) {
//		
//	}

	@Override
	@Transactional(value = "blTransactionManager")
    public void exportOrder(String filePath, String sendDeliveryInfoEmail ) {
        List<Order> orders = this.findNotSentDeliveryInfoOrders();
        if (orders == null || orders.isEmpty()) {
            LOG.info("no order to send delivery info!");
            return;
        }
        // 将订单根据供应商分类
        Map<String, List<String>> providerOrders = new HashMap<String, List<String>>();
        for (Order order : orders) {

            List<OrderItem> ois = orderItemDao.readOrderItemsByOrderId(order.getId());
            if (ois != null && ois.size() > 0) {
                OrderItem oi = ois.get(0);
                if (oi.getSku() != null && oi.getSku().getProduct() != null
                        && oi.getSku().getProduct().getProvider() != null) {
                    String providername = oi.getSku().getProduct().getProvider()
                            .getEnglishName();
                    if (providerOrders.containsKey(providername)) {
                        List<String> os = providerOrders.get(providername);
                        os.add(order.getOrderNumber());
                    } else {
                        List<String> os = new ArrayList<String>();
                        os.add(order.getOrderNumber());
                        providerOrders.put(providername, os);
                    }
                } else {
                    LOG.error("orderitem, it's sku or product or provider is null:"
                            + oi.getId());
                }
            } else {
                LOG.error("order has no order items:" + order.getId());
            }

            try {
                order.setDeliveryInfoSent(true);
                save(order, false);
            } catch (PricingException e) {
                LOG.error("save order error, order id:" + order.getId() + "exception:" + e.getMessage(), e);
            }
        }

        generateExcelAndSendEmail(providerOrders, filePath, sendDeliveryInfoEmail);
    }

	private void generateExcelAndSendEmail(Map<String, List<String>> providerOrders,String filePath, String sendDeliveryInfoEmail ){
		//行高
		int rowHeight = 20;
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmm");
        String fstr = fmt.format(new Date());

        List<Attachment> attachments = new ArrayList<Attachment>();
        
		for (String key : providerOrders.keySet()) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(key);
			
			//设置列宽
			//编号
            sheet.setColumnWidth(0, 5 * 256);
            //订单号
            sheet.setColumnWidth(1, 22 * 256);
            //配送方式
            sheet.setColumnWidth(2, 7 * 256);
            //收货人
            sheet.setColumnWidth(3, 10 * 256);
            //联系方式
            sheet.setColumnWidth(4, 12 * 256);
            //收货地址
            sheet.setColumnWidth(5, 50 * 256);
            //备注
            sheet.setColumnWidth(6, 50 * 256);

            //头部字体
			XSSFFont header_font =workbook.createFont(); 
			header_font.setFontHeightInPoints((short)12);
			
			//内容字体
			XSSFFont content_font =workbook.createFont(); 
			content_font.setFontHeightInPoints((short)10);
			
			//头部样式
			XSSFCellStyle header_style = workbook.createCellStyle();
			header_style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			header_style.setBorderBottom(BorderStyle.THIN);
			header_style.setFont(header_font);
			header_style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			//内容样式，居左
			XSSFCellStyle alignLeft = workbook.createCellStyle();
			alignLeft.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			alignLeft.setVerticalAlignment(VerticalAlignment.CENTER);
			alignLeft.setFont(content_font);
			
			//内容样式，居右
			XSSFCellStyle alignRight = workbook.createCellStyle();
			alignRight.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
			alignRight.setVerticalAlignment(VerticalAlignment.CENTER);
			alignRight.setFont(content_font);
	
			// 头部
            XSSFRow header = sheet.createRow(0);
            header.setHeightInPoints(rowHeight);

            XSSFCell cell0 = header.createCell(0);
            cell0.setCellStyle(header_style);
            cell0.setCellValue("编号");

            XSSFCell cell1 = header.createCell(1);
            cell1.setCellStyle(header_style);
            cell1.setCellValue("订单号");

            XSSFCell cell2 = header.createCell(2);
            cell2.setCellStyle(header_style);
            cell2.setCellValue("配送");

            XSSFCell cell3 = header.createCell(3);
            cell3.setCellStyle(header_style);
            cell3.setCellValue("收货人");

            XSSFCell cell4 = header.createCell(4);
            cell4.setCellStyle(header_style);
            cell4.setCellValue("电话");

            XSSFCell cell5 = header.createCell(5);
            cell5.setCellStyle(header_style);
            cell5.setCellValue("地址");

            XSSFCell cell6 = header.createCell(6);
            cell6.setCellStyle(header_style);
            cell6.setCellValue("备注");

            List<String> orderNumberList = providerOrders.get(key);
            int rowNum = 1;
            for (String oNumber : orderNumberList) {
                Order order = orderDao.readOrderByOrderNumber(oNumber);
                OrderAddress orderAddress = order.getOrderAddress();
                XSSFRow rowi = sheet.createRow(rowNum);
                rowi.setHeightInPoints(rowHeight);

                //编号
                XSSFCell indexCell = rowi.createCell(0);
                indexCell.setCellStyle(alignRight);
                indexCell.setCellValue(rowNum++);

                //订单号
                XSSFCell orderNoCell = rowi.createCell(1);
                orderNoCell.setCellStyle(alignLeft);
                orderNoCell.setCellValue(order.getOrderNumber());

                //配送方式
                XSSFCell deliveryTypeCell = rowi.createCell(2);
                deliveryTypeCell.setCellStyle(alignLeft);
                String deliveryType = DeliveryType.getInstance(order.getDeliveryType()).getFriendlyType();
                deliveryTypeCell.setCellValue(deliveryType);

                //收货人姓名
                XSSFCell receiverCell = rowi.createCell(3);
                receiverCell.setCellStyle(alignLeft);
                receiverCell.setCellValue(orderAddress.getReceiver());

                //联系方式
                XSSFCell phoneCell = rowi.createCell(4);
                phoneCell.setCellStyle(alignLeft);
                phoneCell.setCellValue(orderAddress.getPhone());

                //收货地址
                XSSFCell addressCell = rowi.createCell(5);
                addressCell.setCellStyle(alignLeft);
                addressCell.setCellValue(orderAddress.getProvince() +
                        orderAddress.getCity() +
                        orderAddress.getDistrict() +
                        orderAddress.getCommunity() +
                        orderAddress.getAddress());

                StringBuilder description = new StringBuilder();

                for (OrderItem orderItem : order.getOrderItems()) {
                    description.append(orderItem.getName());
                    description.append(" 数量:");
                    description.append(orderItem.getQuantity());
                    description.append("\n");
                }

                //备注
                XSSFCell remarkCell = rowi.createCell(6);
                remarkCell.setCellStyle(alignLeft);
                remarkCell.setCellValue(description.toString());
            }

            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(filePath + "/shipment_" + key + "(" + fstr + ").xlsx");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
				FileOutputStream fOut = new FileOutputStream(file);
				workbook.write(fOut);
				fOut.flush();
				workbook.close();
				
				FileInputStream fIn = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
				byte[] b = new byte[1000];
				int n;
				while((n = fIn.read(b)) != -1){
					bos.write(b, 0, n);
				}
				fIn.close();
				bos.close();
				Attachment attachment = new Attachment();
				attachment.setFilename(file.getName());
				attachment.setData(bos.toByteArray());
				attachment.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				attachments.add(attachment);

			}catch(IOException e){
				LOG.error("export excel throw exception:"+e.getMessage());
			}
		}
		
		if(attachments.size() > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			EmailInfo emailInfo = sendDeliveryInfoEmailInfo.clone();
			emailInfo.setSubject("出货清单("+fstr+")");
			emailInfo.setAttachments(attachments);
			emailService.sendTemplateEmail(sendDeliveryInfoEmail, emailInfo, props);
		}
	}
	
    @Override
    @Transactional("blTransactionManager")
    public List<Order> readOrderByDateRange(Date from, Date to, OrderStatus stauts) {
        return orderDao.readOrderByDateRange(from, to, stauts);
    }
    
    @Override
    public List<Order> findCompleteOverdueOrders() {
    	return orderDao.findCompleteOverdueOrders();
    }
    
    @Override
    public List<Order> findOverdueOrders() {
        return orderDao.findOverdueOrders();
    }

    @Override
    public List<Order> findNotSentDeliveryInfoOrders() {
        return orderDao.findNotSentDeliveryInfoOrders();
    }

    @Override
    @Transactional("blTransactionManager")
    public List<Order> findNotShippedOrders() {
        return orderDao.findNotShippedOrders();
    }

    @Override
    @Transactional("blTransactionManager")
    public int determineLogisticsOrders(int minCount) {
        return orderDao.determineLogisticsOrders(minCount);
    }

    @Override
    @Transactional("blTransactionManager")
    public int determineExpressOrdersWithCommunity(int minCount) {
        return orderDao.determineExpressOrdersWithCommunity(minCount);
    }

    @Override
    @Transactional("blTransactionManager")
    public int determineExpressOrdersWithoutCommunity() {
        return orderDao.determineExpressOrdersWithoutCommunity();
    }

    @Override
    public OrderDetailToApplyCoupon generateOrderDetailToApplyCoupon(Order order){
    	OrderDetailToApplyCoupon orderDetailToApplyCoupon = new OrderDetailToApplyCoupon(Money.toAmount(order.getSubTotal()));
    	//产品
    	Map<Long, Money> productParameter = new HashMap<Long, Money>();
    	String productIds = "";
    	for(OrderItem orderItem : order.getOrderItems()){
    		Product product = orderItem.getSku().getProduct();
    		if(productParameter.containsKey(product.getId())){
    			productParameter.put(product.getId(),productParameter.get(product.getId()).add(orderItem.getTotalPrice()));
    		}else{
    			productParameter.put(product.getId(), orderItem.getTotalPrice());
    		}
    		productIds =  productIds + product.getId() + ",";
    	}
    	
    	//供应商
    	Map<Long, Money> providerParameter = new HashMap<Long, Money>();
    	String providerIds = "";
    	for(OrderItem orderItem : order.getOrderItems()){
    		Provider provider = orderItem.getSku().getProduct().getProvider();
    		if(providerParameter.containsKey(provider.getId())){
    			providerParameter.put(provider.getId(),providerParameter.get(provider.getId()).add(orderItem.getTotalPrice()));
    		}else{
    			providerParameter.put(provider.getId(), orderItem.getTotalPrice());
    		}
    		providerIds = providerIds + provider.getId() + ",";
    	}
    	
    	//类别
    	Map<Long, Money> categoryParameter = new HashMap<Long, Money>();
    	String categoryIds = "";
    	for(OrderItem orderItem : order.getOrderItems()){
    		List<CategoryProductXref> allParentCategoryXrefs = orderItem.getSku().getProduct().getAllParentCategoryXrefs();
    		for(CategoryProductXref cpx : allParentCategoryXrefs){
    	 		if(categoryParameter.containsKey(cpx.getCategory().getId())){
    	 			categoryParameter.put(cpx.getCategory().getId(),categoryParameter.get(cpx.getCategory().getId()).add(orderItem.getTotalPrice()));
        		}else{
        			categoryParameter.put(cpx.getCategory().getId(), orderItem.getTotalPrice());
        		}
    	 		categoryIds =  categoryIds + cpx.getCategory().getId() + ",";
    		}
   
    		
    	}
    	
    	//添加条件
    	for(Map.Entry<Long, Money> prd : productParameter.entrySet()){
    		orderDetailToApplyCoupon.addCondition(new ProductApplyCondition(prd.getKey().toString() + "|" + prd.getValue().toString()));
    	}
    	
    	for(Map.Entry<Long, Money> pvd : providerParameter.entrySet()){
    		orderDetailToApplyCoupon.addCondition(new ProviderApplyCondition(pvd.getKey().toString() + "|" + pvd.getValue().toString()));
    	}
    	
    	for(Map.Entry<Long, Money> cat : categoryParameter.entrySet()){
    		orderDetailToApplyCoupon.addCondition(new CategoryApplyCondition(cat.getKey().toString() + "|" + cat.getValue().toString()));
    	}
    	orderDetailToApplyCoupon.addCondition(new ProductApplyCondition(productIds));
    	orderDetailToApplyCoupon.addCondition(new ProviderApplyCondition(providerIds));
    	orderDetailToApplyCoupon.addCondition(new CategoryApplyCondition(categoryIds));
    	return orderDetailToApplyCoupon;
    }
    
    @Override
    @Transactional(value = "blTransactionManager")
    public Map<String, List<Order>> getNotShippedProviderOrders() {
        List<Order> orderList = orderDao.findNotShippedOrders();
        // 将订单根据供应商分类
        Map<String, List<Order>> providerOrders = new HashMap<String, List<Order>>();
        for (Order order : orderList) {
            String providername = order.getProvider().getEnglishName();
            if (providerOrders.containsKey(providername)) {
                providerOrders.get(providername).add(order);
            } else {
                List<Order> os = new ArrayList<Order>();
                os.add(order);
                providerOrders.put(providername, os);
            }
        }
        return providerOrders;
    }

	@Override
	public int getSkuCountInOrders(Long skuId, Long customerId) {
		return orderDao.getSkuCountInOrders(skuId, customerId);
	}
}