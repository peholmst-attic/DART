
package net.pkhapps.dart.modules.resources.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             
 * 
 * <p>Java class for resourceInService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resourceInService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.pkhapps.net/dart/resources/1.0}resourceEvent">
 *       &lt;sequence>
 *         &lt;element name="atStation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceInService", namespace = "http://www.pkhapps.net/dart/resources/1.0", propOrder = {
    "atStation"
})
public class ResourceInService
    extends ResourceEvent
{

    @XmlElement(namespace = "http://www.pkhapps.net/dart/resources/1.0")
    protected Boolean atStation;

    /**
     * Gets the value of the atStation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAtStation() {
        return atStation;
    }

    /**
     * Sets the value of the atStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAtStation(Boolean value) {
        this.atStation = value;
    }

}
