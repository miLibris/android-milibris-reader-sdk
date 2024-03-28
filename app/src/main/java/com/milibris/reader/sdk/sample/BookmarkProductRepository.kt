package com.milibris.reader.sdk.sample

import com.milibris.onereader.data.article.IArticle
import com.milibris.onereader.data.error.ReaderError
import com.milibris.onereader.data.session.ReaderSettings
import com.milibris.reader.XmlPdfReaderDataSource

class BookmarkProductRepository(
    readerSettings: ReaderSettings
) : XmlPdfReaderDataSource(readerSettings) {

    /**
     * In-memory but could be replaced with preferences, a database or an api
     */
    private val bookmarkedArticles = mutableListOf<String>()

    override fun getArticle(mid: String): IArticle? =
        super.getArticle(mid)?.also { article ->
            article.isBookmarked = isArticleBookmarked(article.id!!)
        }

    override fun getAllArticles(
        onSuccessListener: (List<IArticle>) -> Unit,
        onErrorListener: (ReaderError) -> Unit
    ) {
        super.getAllArticles({ list ->
            onSuccessListener(list.map {
                it.also { article -> article.isBookmarked = bookmarkedArticles.contains(article.id!!) }
            })
        }, onErrorListener)
    }

    fun toggleBookmarkState(id: String): Boolean {
        if (isArticleBookmarked(id)) bookmarkedArticles.remove(id)
        else bookmarkedArticles.add(id)
        return isArticleBookmarked(id)
    }

    private fun isArticleBookmarked(id: String): Boolean = bookmarkedArticles.contains(id)
}
