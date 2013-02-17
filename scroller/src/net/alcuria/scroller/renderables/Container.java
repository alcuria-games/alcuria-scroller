/**
 * 
 */
package net.alcuria.scroller.renderables;

/**
 * @author juni.kim
 */
public interface Container {
    public boolean addChild(Renderable child);

    public boolean removeChild(Renderable child);

    public Renderable getChild(Renderable child);
}
