package com.hse_miem.fb_miem;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egor on 20/05/16.
 */
public final class TestData {

    public static List<PointF> getNodes() {
        List<PointF> nodes = new ArrayList<>();
        nodes.add(new PointF(190, 273));
        nodes.add(new PointF(190, 173));
        nodes.add(new PointF(95, 173));
        nodes.add(new PointF(95, 90));
        nodes.add(new PointF(143, 90));
        nodes.add(new PointF(190, 90));
        return nodes;
    }

    public static List<PointF> getNodesContacts() {
        List<PointF> nodesContact = new ArrayList<>();
        nodesContact.add(new PointF(0, 1));
        nodesContact.add(new PointF(1, 2));
        nodesContact.add(new PointF(1, 5));
        nodesContact.add(new PointF(2, 3));
        nodesContact.add(new PointF(3, 4));
        nodesContact.add(new PointF(4, 5));
        nodesContact.add(new PointF(5, 1));
        return nodesContact;
    }

}
