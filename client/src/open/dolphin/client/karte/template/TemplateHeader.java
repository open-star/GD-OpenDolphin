
package open.dolphin.client.karte.template;

import java.io.File;

/**
 *
 * @author tomohiro
 */
public class TemplateHeader {

    private String name;
    private String creator;
    private File path;

    /**
     *
     */
    public TemplateHeader() {
        this.name    = null;
        this.creator = null;
        this.path    = null;
    }

    /**
     *
     * @param name
     */
    public TemplateHeader(String name) {
        this.name    = name;
        this.creator = null;
        this.path    = null;
    }

    /**
     *
     * @param name
     * @param creator
     */
    public TemplateHeader(String name, String creator) {
        this.name    = name;
        this.creator = creator;
        this.path    = null;
    }

    /**
     *
     * @param name
     * @param creator
     * @param path
     */
    public TemplateHeader(String name, String creator, File path) {
        this.name    = name;
        this.creator = creator;
        this.path    = path;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param Creator
     */
    public void setCreator(String Creator) {
        this.creator = Creator;
    }

    /**
     *
     * @return
     */
    public String getCreator() {
        return this.creator;
    }

    /**
     *
     * @param path
     */
    public void setPath(File path) {
        this.path = path;
    }

    /**
     *
     * @return
     */
    public File getPath() {
        return this.path;
    }
}
