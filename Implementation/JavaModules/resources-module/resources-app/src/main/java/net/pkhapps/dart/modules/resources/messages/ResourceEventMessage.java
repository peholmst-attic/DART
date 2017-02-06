
package net.pkhapps.dart.modules.resources.messages;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceLocationChanged" type="{http://www.pkhapps.net/dart/resources/1.0}resourceLocationChanged" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceAssigned" type="{http://www.pkhapps.net/dart/resources/1.0}resourceAssigned" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceDispatched" type="{http://www.pkhapps.net/dart/resources/1.0}resourceDispatched" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceEnRoute" type="{http://www.pkhapps.net/dart/resources/1.0}resourceEnRoute" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceOnScene" type="{http://www.pkhapps.net/dart/resources/1.0}resourceOnScene" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceOutOfService" type="{http://www.pkhapps.net/dart/resources/1.0}resourceOutOfService" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceInService" type="{http://www.pkhapps.net/dart/resources/1.0}resourceInService" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resourceLocationChanged",
    "resourceAssigned",
    "resourceDispatched",
    "resourceEnRoute",
    "resourceOnScene",
    "resourceOutOfService",
    "resourceInService"
})
@XmlRootElement(name = "resourceEventMessage", namespace = "http://www.pkhapps.net/dart/resources/1.0")
public class ResourceEventMessage {

    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceLocationChanged> resourceLocationChanged;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceAssigned> resourceAssigned;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceDispatched> resourceDispatched;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceEnRoute> resourceEnRoute;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceOnScene> resourceOnScene;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceOutOfService> resourceOutOfService;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected List<ResourceInService> resourceInService;

    /**
     * Gets the value of the resourceLocationChanged property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceLocationChanged property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceLocationChanged().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceLocationChanged }
     * 
     * 
     */
    public List<ResourceLocationChanged> getResourceLocationChanged() {
        if (resourceLocationChanged == null) {
            resourceLocationChanged = new ArrayList<ResourceLocationChanged>();
        }
        return this.resourceLocationChanged;
    }

    /**
     * Gets the value of the resourceAssigned property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceAssigned property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceAssigned().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceAssigned }
     * 
     * 
     */
    public List<ResourceAssigned> getResourceAssigned() {
        if (resourceAssigned == null) {
            resourceAssigned = new ArrayList<ResourceAssigned>();
        }
        return this.resourceAssigned;
    }

    /**
     * Gets the value of the resourceDispatched property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceDispatched property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceDispatched().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceDispatched }
     * 
     * 
     */
    public List<ResourceDispatched> getResourceDispatched() {
        if (resourceDispatched == null) {
            resourceDispatched = new ArrayList<ResourceDispatched>();
        }
        return this.resourceDispatched;
    }

    /**
     * Gets the value of the resourceEnRoute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceEnRoute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceEnRoute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceEnRoute }
     * 
     * 
     */
    public List<ResourceEnRoute> getResourceEnRoute() {
        if (resourceEnRoute == null) {
            resourceEnRoute = new ArrayList<ResourceEnRoute>();
        }
        return this.resourceEnRoute;
    }

    /**
     * Gets the value of the resourceOnScene property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceOnScene property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceOnScene().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceOnScene }
     * 
     * 
     */
    public List<ResourceOnScene> getResourceOnScene() {
        if (resourceOnScene == null) {
            resourceOnScene = new ArrayList<ResourceOnScene>();
        }
        return this.resourceOnScene;
    }

    /**
     * Gets the value of the resourceOutOfService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceOutOfService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceOutOfService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceOutOfService }
     * 
     * 
     */
    public List<ResourceOutOfService> getResourceOutOfService() {
        if (resourceOutOfService == null) {
            resourceOutOfService = new ArrayList<ResourceOutOfService>();
        }
        return this.resourceOutOfService;
    }

    /**
     * Gets the value of the resourceInService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceInService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceInService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceInService }
     * 
     * 
     */
    public List<ResourceInService> getResourceInService() {
        if (resourceInService == null) {
            resourceInService = new ArrayList<ResourceInService>();
        }
        return this.resourceInService;
    }

}
