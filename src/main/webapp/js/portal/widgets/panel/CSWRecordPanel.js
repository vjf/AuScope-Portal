/**
 * A specialisation of portal.widgets.panel.BaseRecordPanel for rendering
 * records conforming to the portal.csw.CSWRecord Model
 */
Ext.define('portal.widgets.panel.CSWRecordPanel', {
    extend : 'portal.widgets.panel.BaseRecordPanel',

    constructor : function(cfg) {
        this.callParent(arguments);
    },

    /**
     * Implements method - see parent class for details.
     */
    getTitleForRecord : function(record) {
        return record.data.name;
    },

    /**
     * Implements method - see parent class for details.
     */
    getOnlineResourcesForRecord : function(record) {
        return record.data.onlineResources;
    },

    /**
     * Implements method - see parent class for details.
     */
    getSpatialBoundsForRecord : function(record) {
        return record.data.geographicElements;
    }

});