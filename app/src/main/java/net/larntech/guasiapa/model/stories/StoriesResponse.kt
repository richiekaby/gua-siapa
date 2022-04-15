package net.larntech.guasiapa.model.stories

import java.io.Serializable

class StoriesResponse: Serializable {
    /**
     * error : false
     * message : Stories fetched successfully
     * listStory : [{"id":"story-FvU4u0Vp2S3PMsFg","name":"Dimas","description":"Lorem Ipsum","photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png","createdAt":"2022-01-08T06:34:18.598Z","lat":-10.212,"lon":-16.002}]
     */
    var isError = false
    var message: String? = null

    /**
     * id : story-FvU4u0Vp2S3PMsFg
     * name : Dimas
     * description : Lorem Ipsum
     * photoUrl : https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png
     * createdAt : 2022-01-08T06:34:18.598Z
     * lat : -10.212
     * lon : -16.002
     */
    var listStory: List<ListStoryBean>? = null

    class ListStoryBean : Serializable {
        var id: String? = null
        var name: String? = null
        var description: String? = null
        var photoUrl: String? = null
        var createdAt: String? = null
        var lat = 0.0
        var lon = 0.0
    }
}