package cn.globalph.cms.page.message;

import cn.globalph.cms.page.domain.Page;

/**
 * The ArchivedPagePublisher will be notified when a page has
 * been marked as archived.    This provides a convenient cache-eviction
 * point for pages in production.
 *
 * Implementers of this service could send a JMS or AMQP message so
 * that other VMs can evict the item.
 *
 * @Override
 *   public void processPageArchive(final Page page, final String basePageKey) {
 *       archivePageTemplate.send(archivePageDestination, new MessageCreator() {
 *           public Message createMessage(Session session) throws JMSException {
 *               return session.createTextMessage(basePageKey);
 *           }
 *       });
 *   }
 */
public interface ArchivedPagePublisher {
    void processPageArchive(Page page, String basePageKey);
}
