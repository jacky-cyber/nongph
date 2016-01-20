package cn.globalph.admin.server.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.util.BLCCollectionUtils;
import cn.globalph.common.util.TypedTransformer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service("blAdminCatalogService")
public class AdminCatalogServiceImpl implements AdminCatalogService {
    
    private static final Log LOG = LogFactory.getLog(AdminCatalogServiceImpl.class);

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "blSkuDao")
    protected SkuDao skuDao;

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;
    
    @Override
    public Integer generateSkusFromProduct(Long productId) {
        Product product = catalogService.findProductById(productId);
        
        if (CollectionUtils.isEmpty(product.getProductOptions())) {
            return -1;
        }
        
        List<List<ProductOptionValue>> allPermutations = generatePermutations(0, new ArrayList<ProductOptionValue>(), product.getProductOptions());
        LOG.info("Total number of permutations: " + allPermutations.size());
        LOG.info(allPermutations);
        
        //determine the permutations that I already have Skus for
        List<List<ProductOptionValue>> previouslyGeneratedPermutations = new ArrayList<List<ProductOptionValue>>();
        if (CollectionUtils.isNotEmpty(product.getAllSkus())) {
            for (Sku additionalSku : product.getAllSkus()) {
                if (CollectionUtils.isNotEmpty(additionalSku.getProductOptionValues())) {
                    previouslyGeneratedPermutations.add(additionalSku.getProductOptionValues());
                }
            }
        }
        
        List<List<ProductOptionValue>> permutationsToGenerate = new ArrayList<List<ProductOptionValue>>();
        for (List<ProductOptionValue> permutation : allPermutations) {
            boolean previouslyGenerated = false;
            for (List<ProductOptionValue> generatedPermutation : previouslyGeneratedPermutations) {
                if (isSamePermutation(permutation, generatedPermutation)) {
                    previouslyGenerated = true;
                    break;
                }
            }
            
            if (!previouslyGenerated) {
                permutationsToGenerate.add(permutation);
            }
        }
        int numPermutationsCreated = 0;
        //For each permutation, I need them to map to a specific Sku
        for (List<ProductOptionValue> permutation : permutationsToGenerate) {
            if (permutation.isEmpty()) continue;
            Sku permutatedSku = catalogService.createSku();
            permutatedSku.setProduct(product);
            permutatedSku.setProductOptionValues(permutation);
            permutatedSku = catalogService.saveSku(permutatedSku);
            product.getAllSkus().add(permutatedSku);
            numPermutationsCreated++;
        }
        if (numPermutationsCreated != 0) {
            catalogService.saveProduct(product);
        }
        return numPermutationsCreated;
    }
    
    protected boolean isSamePermutation(List<ProductOptionValue> perm1, List<ProductOptionValue> perm2) {
        if (perm1.size() == perm2.size()) {
            
            Collection<Long> perm1Ids = BLCCollectionUtils.collect(perm1, new TypedTransformer<Long>() {
                @Override
                public Long transform(Object input) {
                    return ((ProductOptionValue) input).getId();
                }
            });
            
            Collection<Long> perm2Ids = BLCCollectionUtils.collect(perm2, new TypedTransformer<Long>() {
                @Override
                public Long transform(Object input) {
                    return ((ProductOptionValue) input).getId();
                }
            });
            
            return perm1Ids.containsAll(perm2Ids);
        }
        return false;
    }
    
    /**
     * Generates all the possible permutations for the combinations of given ProductOptions
     * @param currentTypeIndex
     * @param currentPermutation
     * @param options
     * @return a list containing all of the possible combinations of ProductOptionValues based on grouping by the ProductOptionValue
     */
    public List<List<ProductOptionValue>> generatePermutations(int currentTypeIndex, List<ProductOptionValue> currentPermutation, List<ProductOption> options) {
        List<List<ProductOptionValue>> result = new ArrayList<List<ProductOptionValue>>();
        if (currentTypeIndex == options.size()) {
            result.add(currentPermutation);
            return result;
        }
        
        ProductOption currentOption = options.get(currentTypeIndex);
        if (!currentOption.getUseInSkuGeneration()) {
            //This flag means do not generate skus and so do not create permutations for this productoption, 
            //end it here and return the current list of permutations.
            result.addAll(generatePermutations(currentTypeIndex + 1, currentPermutation, options));
            return result;
        }
        for (ProductOptionValue option : currentOption.getAllowedValues()) {
            List<ProductOptionValue> permutation = new ArrayList<ProductOptionValue>();
            permutation.addAll(currentPermutation);
            permutation.add(option);
            result.addAll(generatePermutations(currentTypeIndex + 1, permutation, options));
        }
        if (currentOption.getAllowedValues().size() == 0) {
            //There are still product options left in our array to compute permutations, even though this productOption does not have any values associated.
            result.addAll(generatePermutations(currentTypeIndex + 1, currentPermutation, options));
        }
        
        return result;
    }

    @Override
    public Boolean cloneProduct(Long productId) {
        Product cloneProduct = catalogService.findProductById(productId);
        //initialize the many-to-many to save off
        cloneProduct.getProductOptions().size();
        cloneProduct.getAllParentCategories().size();

        em.detach(cloneProduct);
        cloneProduct.setId(null);
        Product derivedProduct = catalogService.saveProduct(cloneProduct);
        
        cloneProduct = catalogService.findProductById(productId);
        //Re-associate the new Skus to the new Product
        for (Sku additionalSku : cloneProduct.getAllSkus()) {
            additionalSku.getProductOptionValues().size();
            em.detach(additionalSku);
            additionalSku.setId(null);
            additionalSku.setProduct(derivedProduct);
            catalogService.saveSku(additionalSku);
        }
        
        
        return true;
    }
    
}
