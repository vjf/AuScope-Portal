package org.auscope.portal.mineraloccurrence;

import java.util.ArrayList;
import java.util.List;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.server.MineralTenementServiceProviderType;
import org.auscope.portal.core.uifilter.GenericFilter;

/**
 * Class that represents ogc:Filter markup for mt:mineralTenement queries
 *
 * @author Victor Tey
 * @version
 */
public class MineralTenementFilter extends GenericFilter {
    List<String> fragments;

    /**
     * Given set of attributes this object will build a filter to a wild card search for matching tenements
     * Extended to support mt:status as well.
     * @param tenementName name of tenement
     * @param tenementType type of tenement 
     * @param owner owner's name
     * @param size tenement size
     * @param endDate end date
     * @param status tenement status
     * @param optionalFilters filter by attributes other than those above
     * @param mineralTenementServiceProviderType is this service provided by Geoserver or AcrGIS server
     */
    public MineralTenementFilter(String tenementName, String tenementType, String owner, String size, String endDate, String status,
                                 String optionalFilters, MineralTenementServiceProviderType mineralTenementServiceProviderType) {
        super(optionalFilters);
        if (mineralTenementServiceProviderType == null) {
            mineralTenementServiceProviderType = MineralTenementServiceProviderType.GeoServer;
        }
        fragments = new ArrayList<String>();

        if (optionalFilters == null || optionalFilters.isEmpty()) {

            if (tenementName != null && !tenementName.isEmpty()) {
                fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.nameField(), "*" + tenementName + "*"));
            }
            if (tenementType != null && !tenementType.isEmpty()) {
                fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.tenementTypeField(), tenementType));
            }

            if (owner != null && !owner.isEmpty()) {
                fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.ownerField(), owner));
            }

            if (size != null && !size.isEmpty()) {
                fragments.add(this.generatePropertyIsGreaterThanOrEqualTo("mt:area", size));
            }

            if (endDate != null && !endDate.isEmpty()) {
                fragments.add(this.generatePropertyIsLessThanOrEqualTo("mt:expireDate", endDate));
            }

            if (status != null && !status.isEmpty()) {
                fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.statusField(), status));
            }
        } else {
            fragments = this.generateParameterFragments();

        }
    }
    
    @Override
    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
    }

    @Override
    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateBboxFragment(bbox, "mt:shape"));

        return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment
                                                                                                       .size()])));
    }

    public String getFilterWithAdditionalStyle() {

        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateOrComparisonFragment(this.generatePropertyIsLikeFragment("mt:status", "Active"),
                this.generatePropertyIsLikeFragment("mt:status", "GRANTED")));

        return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment
                                                                                                       .size()])));
    }

}
