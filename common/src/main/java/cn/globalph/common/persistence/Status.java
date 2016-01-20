package cn.globalph.common.persistence;

/**
 * Interface that denotes whether or not an entity is archived. Usually, entities that implement this interface also only
 * undergo soft-deletes.
 *  标识活动还是存档
 */
public interface Status {

    public void setArchived(Character archived);

    public Character getArchived();

    public boolean isActive();

}
