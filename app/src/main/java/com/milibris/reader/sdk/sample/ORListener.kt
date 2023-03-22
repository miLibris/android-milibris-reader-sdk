package com.milibris.reader.sdk.sample

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.core.text.HtmlCompat
import co.cafeyn.onereader.data.article.IArticle
import co.cafeyn.onereader.data.article.TextToPlayState
import co.cafeyn.onereader.data.product.Box
import co.cafeyn.onereader.data.session.ReaderListener
import co.cafeyn.onereader.feature.OneReaderActivity
import co.cafeyn.onereader.repository.BaseListener
import com.milibris.reader.XmlPdfReaderDataSource
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset

class ORListener(
    val dataSource: XmlPdfReaderDataSource,
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

    override fun onSummaryOpened(isOpenedFromArticles: Boolean) {
        Log.e(TAG, "onSummaryOpened isOpenedFromArticles:: $isOpenedFromArticles")
    }

    override fun onShareClicked(article: IArticle) {
        val milibrisArticle = dataSource.getMilibrisArticle(article)
        val articleUrl =
            "${dataSource.readerSettings.shareUrl}/share/article/$issueMid/${milibrisArticle?.mid}"
        val intentBuilder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText(articleUrl)
        intentBuilder.startChooser()
    }

    override fun getTextToPlay(article: IArticle): String {

        return article.getTextToPlayFileUrl()?.let { fileUrl ->
            val fileUri = Uri.parse(fileUrl)
            // parsing using an uri in order to have an url starting with /storage instead of file://
            fileUri.path?.let {
                val content = FileUtils.readFileToString(File(it), Charset.defaultCharset())
                val startHead = content.indexOf("<head>")
                val endHead = content.indexOf("</head>") + 7
                val contentNoHead = String.format(
                    "%s%s",
                    content.substring(0, startHead),
                    content.substring(endHead)
                )
                HtmlCompat.fromHtml(contentNoHead, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString().replace("[\\n\\r\\t]+", "")
            } ?: ""
        } ?: ""
    }

    override fun onArticlesOpened(article: IArticle) {
        Log.e(TAG, "ORListener :: article title  ${article.title}")
    }

    override fun onArticleChange(article: IArticle) {
        Log.e(TAG, "onArticleChange :: article title  ${article.title}")
    }

    override fun onArticleBookMarkClicked(article: IArticle, isFromArticleView: Boolean, bookmarkListener: BaseListener<Boolean>) {
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

    override fun shouldOpenArticle(oneReaderActivity: OneReaderActivity, article: IArticle): Boolean {
        Log.e("ORListener", "shouldOpenArticle")
        return true
    }
}
