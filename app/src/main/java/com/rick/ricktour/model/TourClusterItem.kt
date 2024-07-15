package com.rick.ricktour.model

import com.rick.ricktour.api.TourItem
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

//ClusterItem代表地圖上的一個marker
class TourClusterItem(var item: TourItem) : ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(item.nlat ?: 0.0, item.elong ?: 0.0)
    }

    override fun getTitle(): String? {
        return item.name
    }

    override fun getSnippet(): String? {
        return item.name
    }

}