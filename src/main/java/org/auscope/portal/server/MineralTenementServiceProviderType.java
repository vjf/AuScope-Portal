package org.auscope.portal.server;

import java.util.ArrayList;
import java.util.List;

import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.responses.csw.AbstractCSWOnlineResource;
import org.auscope.portal.core.services.responses.csw.AbstractCSWOnlineResource.OnlineResourceType;

/**
 * Differentiates WMS service providers for Mineral Tenement services.
 * 
 * The behaviour of ArcGIS Server is different to the default (GeoServer) and this
 * enum provides settings for each such that they behave in the same way to the
 * end user.
 * 
 */
public enum MineralTenementServiceProviderType {
    GeoServer("mt:MineralTenement", "#66ff66", "#4B6F44", "mt:name", "mt:owner", "mt:shape","Polygon for mineral tenement","mt:tenementType", "mt:status"),
    ArcGIS("MineralTenement", "#00ff00", "#66ff66", "TENNAME", "TENOWNER", "SHAPE","", "TENTYPE", "TENSTATUS");

    private final String featureType;
    private final String fillColour;
    private final String borderColour;
    private final String nameField;
    private final String ownerField;
    private final String shapeField;
    private final String styleName;
    private final String tenementTypeField;
    private final String statusField;



    private MineralTenementServiceProviderType(String featureType, String fillColour, String borderColour, String tenementName, String owner,
                String shape, String style, String tenementType, String status) {
        this.featureType = featureType;
        this.fillColour = fillColour;
        this.borderColour = borderColour;
        this.nameField = tenementName;
        this.ownerField = owner;
        this.shapeField = shape;
        this.styleName = style;
        this.tenementTypeField = tenementType;
        this.statusField = status;
    }

    public String featureType() {
        return featureType;
    }

    public String fillColour() {
        return fillColour;
    }

    public String borderColour() {
        return borderColour;
    }

    public String nameField() {
        return nameField;
    }

    public String ownerField() {
        return ownerField;
    }

    public String shapeField() {
        return shapeField;
    }
    
    public String styleName() {
        return styleName;
    }
    
    public String tenementTypeField() {
        return tenementTypeField;
    }
    
    public String statusField() {
        return statusField;
    }

    /*
     * Function that recognises ArcGIS services by examining the service URL
     *
     * @param serviceUrl WMS service URL
     * @param cswService CSW cache service
     * @returns mineral service provider type, either .ArcGIS or .GeoServer
     */
    public static MineralTenementServiceProviderType parseUrl(String serviceUrl, CSWCacheService cswService) {
        if (serviceUrl != null) {
            // Looking for ArcGIS server in CSW Cache records
            for (CSWRecord record : cswService.getWMSRecords()) {
                for (AbstractCSWOnlineResource resource : record.getOnlineResourcesByType(OnlineResourceType.WMS)) {
                    if (resource.getApplicationProfile().equals("Esri:ArcGIS Server") && serviceUrl.equals(resource.getLinkage().toString())) {
                        return MineralTenementServiceProviderType.ArcGIS;    
                    }
                }            
            }
        }
        return MineralTenementServiceProviderType.GeoServer;
    }
}
