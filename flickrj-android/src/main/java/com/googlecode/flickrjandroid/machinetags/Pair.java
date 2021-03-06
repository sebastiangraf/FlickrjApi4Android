package com.googlecode.flickrjandroid.machinetags;

/**
 * 
 * @author mago
 * @version $Id: Pair.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class Pair implements ITag {
	public static final long serialVersionUID = 12L;

    private String namespace;
    private String predicate;
    private int usage;
    
    /**
	 * 
	 */
	public Pair() {
		super();
	}

	public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(String predicates) {
        try {
            setUsage(Integer.parseInt(predicates));
        } catch (NumberFormatException e) {}
    }

	public void setUsage(int usage) {
		this.usage = usage;
	}

    public String getValue() {
    	return namespace + ":" + predicate;
    }
}
