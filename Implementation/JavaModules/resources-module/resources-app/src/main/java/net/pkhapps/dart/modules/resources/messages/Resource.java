
package net.pkhapps.dart.modules.resources.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *             
 * 
 * <p>Java class for resource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}ID"/>
 *         &lt;element name="callSign" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resourceTypeId" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="status" type="{http://www.pkhapps.net/dart/resources/1.0}resourceStatus" minOccurs="0"/>
 *         &lt;element name="location" type="{http://www.pkhapps.net/dart/resources/1.0}resourceLocation" minOccurs="0"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource", namespace = "http://www.pkhapps.net/dart/resources/1.0", propOrder = {
    "id",
    "callSign",
    "resourceTypeId",
    "status",
    "location",
    "active"
})
public class Resource {

    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0", required = true)
    protected String callSign;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String resourceTypeId;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected ResourceStatus status;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected ResourceLocation location;
    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected boolean active;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the callSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * Sets the value of the callSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallSign(String value) {
        this.callSign = value;
    }

    /**
     * Gets the value of the resourceTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceTypeId() {
        return resourceTypeId;
    }

    /**
     * Sets the value of the resourceTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceTypeId(String value) {
        this.resourceTypeId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceStatus }
     *     
     */
    public ResourceStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceStatus }
     *     
     */
    public void setStatus(ResourceStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLocation }
     *     
     */
    public ResourceLocation getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLocation }
     *     
     */
    public void setLocation(ResourceLocation value) {
        this.location = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

}
