
package net.pkhapps.dart.modules.resources.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             
 * 
 * <p>Java class for resourceLocationChanged complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resourceLocationChanged">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.pkhapps.net/dart/resources/1.0}resourceEvent">
 *       &lt;sequence>
 *         &lt;element name="location" type="{http://www.pkhapps.net/dart/resources/1.0}coordinates"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceLocationChanged", namespace = "http://www.pkhapps.net/dart/resources/1.0", propOrder = {
    "location"
})
public class ResourceLocationChanged
    extends ResourceEvent
{

    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0", required = true)
    protected Coordinates location;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Coordinates }
     *     
     */
    public Coordinates getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coordinates }
     *     
     */
    public void setLocation(Coordinates value) {
        this.location = value;
    }

}
