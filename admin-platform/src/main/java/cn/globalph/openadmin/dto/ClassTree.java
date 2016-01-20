package cn.globalph.openadmin.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClassTree implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String classname;
    protected String name;
    protected String friendlyName;
    protected ClassTree[] children = new ClassTree[0];
    protected int left;
    protected int right;
    protected boolean excludeFromPolymorphism;

    public ClassTree(String fullyQualifiedClassname, String friendlyName, boolean excludeFromPolymorphism) {
        setClassname(fullyQualifiedClassname);
        this.friendlyName = friendlyName;
        this.excludeFromPolymorphism = excludeFromPolymorphism;
    }

    public ClassTree(String fullyQualifiedClassname, boolean excludeFromPolymorphism) {
        this(fullyQualifiedClassname, null, excludeFromPolymorphism);
    }

    public ClassTree(String fullyQualifiedClassname) {
        this(fullyQualifiedClassname, null, false);
    }

    public ClassTree() {
        //do nothing
    }

    public boolean hasChildren() {
        return children.length > 0;
    }

    public int finalizeStructure(int start) {
        left = start;
        start++;
        for (int i = children.length-1;i >= 0; i--) {
            start = children[i].finalizeStructure(start);
            start++;
        }
        right = start;

        return start;
    }

    public List<ClassTree> getCollapsedClassTrees() {
        List<ClassTree> list = new ArrayList<ClassTree>();
        addChildren(this, list);
        return list;
    }

    protected void addChildren(ClassTree tree, List<ClassTree> list) {
        if (!tree.isExcludeFromPolymorphism()) {
            list.add(tree);
        }

        for (ClassTree child : tree.getChildren()) {
            addChildren(child, list);
        }
    }

    public ClassTree find(String fullyQualifiedClassname) {
        if (this.classname.equals(fullyQualifiedClassname)) {
            return this;
        }
        ClassTree result = null;
        for (ClassTree child : children) {
            result = child.find(fullyQualifiedClassname);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String fullyQualifiedClassname) {
        this.classname = fullyQualifiedClassname;
        int pos = fullyQualifiedClassname.lastIndexOf('.');
        if (pos >= 0) {
            name = fullyQualifiedClassname.substring(pos + 1, fullyQualifiedClassname.length());
        } else {
            name = fullyQualifiedClassname;
        }
    }

    public String getFriendlyName() {
        return friendlyName == null ? name : friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public ClassTree[] getChildren() {
        return children;
    }

    public void setChildren(ClassTree[] children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public boolean isExcludeFromPolymorphism() {
        return this.excludeFromPolymorphism;
    }
}
