package cn.globalph.cms.structure.message;

import cn.globalph.cms.structure.domain.StructuredContent;

/**
 * The ArchivedStructuredContentPublisher will be notified when a StructuredContent item has
 * been marked as archived.    This provides a convenient cache-eviction
 * point for items in production.
 *
 * Implementers of this service could send a JMS or AMQP message so
 * that other VMs can evict the item.
 *
 * @Override
 *   public void processStructuredContentArchive(final StructuredContent sc, final String baseNameKey, final String baseTypeKey) {
 *      archiveStructuredContentTemplate.send(archiveStructuredContentDestination, new MessageCreator() {
 *           public Message createMessage(Session session) throws JMSException {
 *               HashMap<String, String> objectMap = new HashMap<String,String>(2);
 *               objectMap.put("nameKey", baseNameKey);
 *               objectMap.put("typeKey", baseTypeKey);
 *               return session.createObjectMessage(objectMap);
 *           }
 *       });
 *   }.
 */
public interface ArchivedStructuredContentPublisher {
    void processStructuredContentArchive(StructuredContent sc, String baseTypeKey, String baseNameKey);
}
