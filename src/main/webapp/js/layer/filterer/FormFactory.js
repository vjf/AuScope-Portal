/**
 * A FormFactory is a Factory class for generating instances of portal.layer.filterer.forms.BaseFilterForm
 * that are appropriate for a given portal.layer.Layer
 */
Ext.define('portal.layer.filterer.FormFactory', {
    extend : 'Ext.util.Observable',

    constructor : function(config) {
        this.listeners = config.listeners;
        this.callParent(config);
    },

    /**
     * Utility function for creating the return type
     */
    _generateResult : function(form, supportsFiltering) {
        return {
            form                : form,
            supportsFiltering   : supportsFiltering
        };
    },

    /**
     * Given an portal.layer.Layer, work out whether there is an appropriate portal.layer.filterer.BaseFilterForm to show
     *
     * Returns a response in the form
     * {
     *    form : Ext.FormPanel - can be null - the formpanel to be displayed when this layer is selected
     *    supportsFiltering : boolean - whether this formpanel supports the usage of the filter button
     * }
     *
     */
    getFilterForm : function(layer) {
        var baseFilterForm = null;
        var baseFilterFormCfg = {
            layer : layer,
            id : layer.id
        };

        //A number of known layer's have specific filter forms
        if (layer.sourceType === portal.layer.Layer.KNOWN_LAYER) {
            switch (layer.source.id) {
            case 'pressuredb-borehole':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.PressureDBFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'nvcl-borehole':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.NvclFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-mine':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.MineFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-miningactivity':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.MiningActivityFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-mineraloccurrence':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.MineralOccurrenceFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'yilgarn-geochem':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.YilgarnGeochemistryFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'gsml-borehole':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.BoreholeFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'portal-reports':
                baseFilterForm = Ext.create('portal.layer.filterer.forms.ReportFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            }
        }

        //otherwise let's see if we can guess an appropriate filter based on layer renderer
        if (layer.renderer instanceof portal.layer.renderer.wms.WMSRenderer) {
            baseFilterForm = Ext.create('portal.layer.filterer.forms.WMSLayerFilterForm', baseFilterFormCfg);
            return this._generateResult(baseFilterForm, true);
        }

        //And otherwise we just show no filter form
        return this._generateResult(null, false);
    }
});