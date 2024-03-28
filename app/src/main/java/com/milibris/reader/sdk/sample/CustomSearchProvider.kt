package com.milibris.reader.sdk.sample

import android.net.Uri
import com.milibris.onereader.feature.search.SearchProvider
import com.milibris.onereader.feature.search.model.SearchResponse
import com.milibris.onereader.feature.search.model.SearchResponseItem
import com.milibris.onereader.feature.search.model.SearchResponseItemArticle

class CustomSearchProvider : SearchProvider {

    private val maecenasArticle = object : SearchResponseItemArticle {
        override val mid: String
            get() = "42d5551d-f22e-4f55-9881-8731da68ec75"
        override val uri: Uri
            get() = Uri.parse("../content/articles/c3fd74c7-0d4b-43e0-a86e-2e2fce4385cd.html")
        override val jsonUri: Uri
            get() = Uri.parse("../content/articles/c3fd74c7-0d4b-43e0-a86e-2e2fce4385cd.json")
        override val title: String
            get() = "<em class=\"highlight\">Maecenas</em> aliquam eu vehicula lectus velit"
        override val rubric: String
            get() = ""
        override val readingTime: Int
            get() = 1
        override val firstPageNumber: Int
            get() = 10
        override val lastPageNumber: Int
            get() = firstPageNumber
        override val abbrev: String
            get() = "Praesent malesuada. bibendum. Donec feugiat tempor libero. Nam uut, massa. Maecenas vitae ante et lacus aliquam hendrerit. Curabitur nunc eros, euismod in, convallis at, vehicula sed consectetuer posuere, eros mauris dignissim diam, pretium sed pede suscipit: Adiam condimentum purus, in consectetuer Proin in sapien. Fusce urna magna,neque eget lacus. Maecenas felis nunc, aliquam ac, consequat vita"

        override fun getThumbnailPath(): String? = null
    }

    override fun search(searchText: String, completion: (Result<SearchResponse>) -> Unit) {
        when (searchText.lowercase()) {
            "server error" ->
                completion(Result.failure(Exception("A server error occurred")))
            "maecenas" ->
                completion(
                    Result.success(
                        SearchResponse(
                            listOf(
                                SearchResponseItem(
                                    maecenasArticle,
                                    listOf(maecenasArticle.abbrev.replace("Maecenas", """<em class="highlight">Maecenas</em>"""))
                                )
                            ),
                            emptyList()
                        )
                    )
                )
            else -> completion(Result.success(SearchResponse(emptyList(), emptyList())))
        }
    }
}
