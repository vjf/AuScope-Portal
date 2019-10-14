package org.auscope.portal.mineraloccurrence;

import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.server.domain.ogc.AbstractFilterTestUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;


public class TestMineralTenementFilter extends PortalTestClass {
    /**
     * Test with commodity
     * 
     * @throws Exception
     */
    @Test
    public void testMinTenFilter() throws Exception {
        MineralTenementFilter filter = new MineralTenementFilter("abc", "def", "ghi", "jkl", "mno", "pqr", null, null);

        String result = filter.getFilterStringAllRecords();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(result);
        
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:Literal",
                new String[] {"*abc*", "def", "ghi", "pqr"}, 4);

    }

    /**
     * Test without commodity. Should return a empty string.
     * 
     * @throws Exception
     */
    @Test
    public void testEmptyTenFilter() throws Exception {
        MineralTenementFilter filter = new MineralTenementFilter("", "", "", "", "", "", "", null);

        String result = filter.getFilterStringAllRecords();
        Assert.assertTrue(result.isEmpty());

    }

    /**
     * Test without commodity. Should return a empty string.
     * 
     * @throws Exception
     */
    @Test
    public void testAdditionalStyle() throws Exception {
        MineralTenementFilter filter = new MineralTenementFilter("abc", "def", "ghi", "jkl", "mno", "pqr", null, null);

        String result = filter.getFilterWithAdditionalStyle();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(result);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:Literal",
                new String[] {"*abc*", "def", "ghi", "pqr", "Active", "GRANTED"}, 6);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc,
                "/descendant::ogc:PropertyIsGreaterThanOrEqualTo/ogc:Literal",
                new String[] {"jkl"}, 1);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc,
                "/descendant::ogc:PropertyIsLessThanOrEqualTo/ogc:Literal",
                new String[] {"mno"}, 1);

    }
}
