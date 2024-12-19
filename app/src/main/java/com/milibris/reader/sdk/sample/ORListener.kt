package com.milibris.reader.sdk.sample

import android.content.Context
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.fragment.app.FragmentActivity
import com.milibris.onereader.data.article.IArticle
import com.milibris.onereader.data.article.TextToPlayState
import com.milibris.onereader.data.product.Box
import com.milibris.onereader.data.session.ReaderListener
import com.milibris.onereader.repository.BaseListener

class ORListener(
    private val productRepo: BookmarkProductRepository,
    private val issueMid: String,
    private val context: Context
) : ReaderListener {

    companion object {
        val TAG: String = ORListener::class.java.simpleName
    }
    private var targetPage: Int = 0

    override fun onCloseButtonClickListener() {
        Log.e(TAG, "onCloseButtonClickListener Called")
    }

    override fun onIssueRead() {
        Log.e(TAG, "onIssueRead")
    }

    override fun onIssuePageRead(pageNumber: Int, isCalledFromArticles: Boolean) {
        targetPage = pageNumber
        Log.e(TAG, "onIssuePageRead at page : $pageNumber")
    }

    override fun onIssuePagePaused(pageNumber: Int) {
        super.onIssuePagePaused(pageNumber)
        Log.e(TAG, "onIssuePagePaused at page : $pageNumber")
    }

    override fun onIssuePageResumed(pageNumber: Int) {
        super.onIssuePageResumed(pageNumber)
        Log.e(TAG, "onIssuePageResumed at page : $pageNumber")
    }

    override fun onIssuePageReadAfter(pageNumber: Int, isCalledFromArticles: Boolean) {
        Log.e(TAG, "onIssuePageReadAfter at page : $pageNumber")
    }

    override fun onSummaryOpened(isOpenedFromArticles: Boolean) {
        Log.e(TAG, "onSummaryOpened isOpenedFromArticles:: $isOpenedFromArticles")
    }

    override fun onShareClicked(article: IArticle) {
        val milibrisArticle = productRepo.getMilibrisArticle(article)
        val articleUrl =
            "${productRepo.readerSettings.shareUrl}/share/article/$issueMid/${milibrisArticle?.mid}"
        val intentBuilder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText(articleUrl)
        intentBuilder.startChooser()
    }

    override fun onArticlesOpened(article: IArticle) {
        Log.e(TAG, "onArticlesOpened :: article title  ${article.title}")
    }

    override fun onArticlePaused(article: IArticle) {
        super.onArticlePaused(article)
        Log.e(TAG, "onArticlePaused :: article title  ${article.title}")
    }

    override fun onArticleResumed(article: IArticle) {
        super.onArticleResumed(article)
        Log.e(TAG, "onArticleResumed :: article title  ${article.title}")
    }

    override fun onArticleChange(article: IArticle) {
        Log.e(TAG, "onArticleChange :: article title  ${article.title}")
    }

    override fun onArticlesClosed(article: IArticle) {
        Log.e(TAG, "onArticlesClosed :: article title  ${article.title}")
    }

    override fun onMiniSummaryOpened() {
        Log.e(TAG, "onMiniSummaryOpened")
    }

    override fun onTextToPlayStateChanged(article: IArticle, state: TextToPlayState) {
        Log.e(TAG, "onTextToPlayListener: article: ${article.id}, state : $state")
    }

    override fun onEnrichmentClicked(box: Box) {
        Log.e("ORListener", "onEnrichmentClicked")
    }

    override fun shouldOpenArticle(oneReaderActivity: FragmentActivity, article: IArticle): Boolean {
        Log.e("ORListener", "shouldOpenArticle")
        return true
    }

    // region Bookmark
    override fun onArticleBookMarkClicked(article: IArticle, isFromArticleView: Boolean, bookmarkListener: BaseListener<Boolean>) {
        val isBookmarked = productRepo.toggleBookmarkState(article.id!!)
        bookmarkListener.onSuccessListener(isBookmarked)
    }
    // endregion
}
