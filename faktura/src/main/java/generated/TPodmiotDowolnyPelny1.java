//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.09 at 12:19:45 PM CEST 
//


package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Pe�ny zestaw danych o osobie fizycznej lub niefizycznej - bez elementu Poczta w adresie polskim
 *
 * <p>Java class for TPodmiotDowolnyPelny1 complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TPodmiotDowolnyPelny1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="OsobaFizyczna" type="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TIdentyfikatorOsobyFizycznejPelny"/>
 *           &lt;element name="OsobaNiefizyczna" type="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TIdentyfikatorOsobyNiefizycznejPelny"/>
 *         &lt;/choice>
 *         &lt;element name="AdresZamieszkaniaSiedziby">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TAdres1">
 *                 &lt;attribute name="rodzajAdresu" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="RAD" />
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPodmiotDowolnyPelny1", propOrder = {
        "osobaFizyczna",
        "osobaNiefizyczna",
        "adresZamieszkaniaSiedziby"
})
public class TPodmiotDowolnyPelny1 {

    @XmlElement(name = "OsobaFizyczna")
    protected TIdentyfikatorOsobyFizycznejPelny osobaFizyczna;
    @XmlElement(name = "OsobaNiefizyczna")
    protected TIdentyfikatorOsobyNiefizycznejPelny osobaNiefizyczna;
    @XmlElement(name = "AdresZamieszkaniaSiedziby", required = true)
    protected AdresZamieszkaniaSiedziby adresZamieszkaniaSiedziby;

    /**
     * Gets the value of the osobaFizyczna property.
     *
     * @return possible object is
     * {@link TIdentyfikatorOsobyFizycznejPelny }
     */
    public TIdentyfikatorOsobyFizycznejPelny getOsobaFizyczna() {
        return osobaFizyczna;
    }

    /**
     * Sets the value of the osobaFizyczna property.
     *
     * @param value allowed object is
     *              {@link TIdentyfikatorOsobyFizycznejPelny }
     */
    public void setOsobaFizyczna(TIdentyfikatorOsobyFizycznejPelny value) {
        this.osobaFizyczna = value;
    }

    /**
     * Gets the value of the osobaNiefizyczna property.
     *
     * @return possible object is
     * {@link TIdentyfikatorOsobyNiefizycznejPelny }
     */
    public TIdentyfikatorOsobyNiefizycznejPelny getOsobaNiefizyczna() {
        return osobaNiefizyczna;
    }

    /**
     * Sets the value of the osobaNiefizyczna property.
     *
     * @param value allowed object is
     *              {@link TIdentyfikatorOsobyNiefizycznejPelny }
     */
    public void setOsobaNiefizyczna(TIdentyfikatorOsobyNiefizycznejPelny value) {
        this.osobaNiefizyczna = value;
    }

    /**
     * Gets the value of the adresZamieszkaniaSiedziby property.
     *
     * @return possible object is
     * {@link TPodmiotDowolnyPelny1 .AdresZamieszkaniaSiedziby }
     */
    public AdresZamieszkaniaSiedziby getAdresZamieszkaniaSiedziby() {
        return adresZamieszkaniaSiedziby;
    }

    /**
     * Sets the value of the adresZamieszkaniaSiedziby property.
     *
     * @param value allowed object is
     *              {@link TPodmiotDowolnyPelny1 .AdresZamieszkaniaSiedziby }
     */
    public void setAdresZamieszkaniaSiedziby(AdresZamieszkaniaSiedziby value) {
        this.adresZamieszkaniaSiedziby = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2018/08/24/eD/DefinicjeTypy/}TAdres1">
     *       &lt;attribute name="rodzajAdresu" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="RAD" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class AdresZamieszkaniaSiedziby
            extends TAdres1 {

        @XmlAttribute(name = "rodzajAdresu", required = true)
        protected String rodzajAdresu;

        /**
         * Gets the value of the rodzajAdresu property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getRodzajAdresu() {
            if (rodzajAdresu == null) {
                return "RAD";
            } else {
                return rodzajAdresu;
            }
        }

        /**
         * Sets the value of the rodzajAdresu property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setRodzajAdresu(String value) {
            this.rodzajAdresu = value;
        }

    }

}
