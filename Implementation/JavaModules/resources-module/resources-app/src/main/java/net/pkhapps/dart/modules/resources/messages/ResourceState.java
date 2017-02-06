
package net.pkhapps.dart.modules.resources.messages;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resourceState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="resourceState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN_SERVICE_AT_STATION"/>
 *     &lt;enumeration value="IN_SERVICE_OVER_RADIO"/>
 *     &lt;enumeration value="ASSIGNED_AT_STATION"/>
 *     &lt;enumeration value="ASSIGNED_OVER_RADIO"/>
 *     &lt;enumeration value="DISPATCHED_AT_STATION"/>
 *     &lt;enumeration value="DISPATCHED_OVER_RADIO"/>
 *     &lt;enumeration value="EN_ROUTE"/>
 *     &lt;enumeration value="ON_SCENE"/>
 *     &lt;enumeration value="OUT_OF_SERVICE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "resourceState", namespace = "http://www.pkhapps.net/dart/resources/1.0")
@XmlEnum
public enum ResourceState {

    IN_SERVICE_AT_STATION,
    IN_SERVICE_OVER_RADIO,
    ASSIGNED_AT_STATION,
    ASSIGNED_OVER_RADIO,
    DISPATCHED_AT_STATION,
    DISPATCHED_OVER_RADIO,
    EN_ROUTE,
    ON_SCENE,
    OUT_OF_SERVICE;

    public String value() {
        return name();
    }

    public static ResourceState fromValue(String v) {
        return valueOf(v);
    }

}
