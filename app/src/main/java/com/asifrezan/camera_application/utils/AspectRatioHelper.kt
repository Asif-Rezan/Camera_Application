package com.asifrezan.camera_application.utils

import com.asifrezan.camera_application.models.PhotoType

object AspectRatioHelper {
    fun getAspectRatio(photoType: PhotoType): Pair<Int, Int> {
        return when (photoType) {
            PhotoType.ID_PHOTO -> Pair(3, 2)
            PhotoType.MEMBER_PHOTO -> Pair(4, 3)
            PhotoType.ID_MEMBER_COMBO -> Pair(16, 9)
        }
    }
}