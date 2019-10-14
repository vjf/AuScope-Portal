package org.auscope.portal.mineraloccurrence;

import org.auscope.portal.server.MineralTenementServiceProviderType;

/**
 * Class that represents ogc:Filter markup for mt:mineralTenement queries with CCStatus&CCType supporting 
 *
 * @author Lingbo Jiang
 * @version
 */
public class MineralTenementCCFilter extends  MineralTenementFilter {

    private MineralTenementServiceProviderType mineralTenementServiceProviderType;
    
    public MineralTenementCCFilter(String name, String tenementType, String owner, String size, String endDate, MineralTenementServiceProviderType serviceProviderType) {
        super(name, tenementType, owner, size, endDate, null, null, serviceProviderType);
        this.mineralTenementServiceProviderType = serviceProviderType;
    }


    /**
     * Given a property, a property value, this method will create a filter
     *
     * @param ccProperty
     *            ccProperty value, either "TenementType" or "TenementStatus"
     * @param ccPropertyValue
     *            the value of the "mt:status" or "mt:tenementType" property for geoserver
     *            or the value of the "TENSTATUS" or "TENTYPE" property for arcgis server
     * @return 
     */
    public void addCCPropertyInFilter(String ccProperty, String ccPropertyValue) {
        if (ccPropertyValue != null && !ccPropertyValue.isEmpty()) {
            // ArcGIS Server only does 'is equal to'
            if (this.mineralTenementServiceProviderType == MineralTenementServiceProviderType.ArcGIS) {
                if(ccProperty.contains("TenementType")){
                    fragments.add(this.generatePropertyIsEqualToFragment(mineralTenementServiceProviderType.tenementTypeField(), ccPropertyValue));
                } else if (ccProperty.contains("TenementStatus")){
                    fragments.add(this.generatePropertyIsEqualToFragment(mineralTenementServiceProviderType.statusField(), ccPropertyValue));
                }
            // Geoserver
            } else {
                if(ccProperty.contains("TenementType")){
                    fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.tenementTypeField(), ccPropertyValue));
                } else if (ccProperty.contains("TenementStatus")){
                    fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.statusField(), ccPropertyValue));
                }
            }
        }
    }
}
