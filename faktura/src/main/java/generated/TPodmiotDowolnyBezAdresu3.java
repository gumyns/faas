//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.09 at 12:19:45 PM CEST 
//


package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Skr�cony zestaw danych o osobie fizycznej lub niefizycznej z identyfikatorem NIP - bez elementu numer REGON dla osoby niefizycznej
 *
 * <p>Java class for TPodmiotDowolnyBezAdresu3 complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TPodmiotDowolnyBezAdresu3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="OsobaFizyczna" type="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TIdentyfikatorOsobyFizycznej2"/>
 *         &lt;element name="OsobaNiefizyczna" type="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TIdentyfikatorOsobyNiefizycznej1"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPodmiotDowolnyBezAdresu3", propOrder = {
        "osobaFizyczna",
        "osobaNiefizyczna"
})
@XmlSeeAlso({
        TPodmiotDowolny2.class
})
public class TPodmiotDowolnyBezAdresu3 {

    @XmlElement(name = "OsobaFizyczna")
    protected TIdentyfikatorOsobyFizycznej2 osobaFizyczna;
    @XmlElement(name = "OsobaNiefizyczna")
    protected TIdentyfikatorOsobyNiefizycznej1 osobaNiefizyczna;

    /**
     * Gets the value of the osobaFizyczna property.
     *
     * @return possible object is
     * {@link TIdentyfikatorOsobyFizycznej2 }
     */
    public TIdentyfikatorOsobyFizycznej2 getOsobaFizyczna() {
        return osobaFizyczna;
    }

    /**
     * Sets the value of the osobaFizyczna property.
     *
     * @param value allowed object is
     *              {@link TIdentyfikatorOsobyFizycznej2 }
     */
    public void setOsobaFizyczna(TIdentyfikatorOsobyFizycznej2 value) {
        this.osobaFizyczna = value;
    }

    /**
     * Gets the value of the osobaNiefizyczna property.
     *
     * @return possible object is
     * {@link TIdentyfikatorOsobyNiefizycznej1 }
     */
    public TIdentyfikatorOsobyNiefizycznej1 getOsobaNiefizyczna() {
        return osobaNiefizyczna;
    }

    /**
     * Sets the value of the osobaNiefizyczna property.
     *
     * @param value allowed object is
     *              {@link TIdentyfikatorOsobyNiefizycznej1 }
     */
    public void setOsobaNiefizyczna(TIdentyfikatorOsobyNiefizycznej1 value) {
        this.osobaNiefizyczna = value;
    }

}
