package com.milibris.reader.sdk.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.updateLayoutParams
import com.milibris.onereader.data.article.IArticle
import com.milibris.onereader.data.session.ReaderSettings
import com.milibris.onereader.feature.OneReaderActivity
import com.milibris.reader.advert.MiLibrisPageAdRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

/**
 * Main activity unpacking and reading a content with PDF reader.
 *
 *
 * Milibris
 */
class MainActivity : AppCompatActivity() {
    private lateinit var productRepo: BookmarkProductRepository
    private val pdfName = "milibris_sample"
    private var coverRatio: Float = 0f
    private var targetPage: Int = 1
    private val readerSettings = ReaderSettings().apply {
        isFaceCropEnabled = true
        debugBoxes = false
        targetPageNumber = targetPage
        textToSpeechEnabled = true
        shareEnabled = true
        enableSummaryImages = true
        logo = R.drawable.milibris
        logoLight = R.drawable.milibris_light
        logoDark = R.drawable.milibris_dark
        isSummaryEnabled = true
        isPrintEnabled = true

        /**
         * Force a layout on all articles
         */
        articleForceLayout = IArticle.Layout.DEFAULT

        /**
         * Time a page stays visible before [ORListener.onIssuePageReadAfter] is called
         */
        issuePageReadAfter = TimeUnit.SECONDS.toMillis(3)

        /**
         * Enable bookmark feature
         */
        bookmarkEnabled = true
    }
    private lateinit var coverImageURL: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareProductRepo()
        prepareCoverToTestSharedElementTransition()
    }

    private fun prepareCoverToTestSharedElementTransition() {
        productRepo.getResourceThumbnailUri(
            1,
            {
                coverImageURL = it.toString()
                findViewById<ImageView>(R.id.coverImage).apply {
                    val firstPage = productRepo.materialParser.mPages[0]
                    coverRatio = firstPage.width.toFloat() / firstPage.height.toFloat()
                    updateLayoutParams<ConstraintLayout.LayoutParams> {
                        dimensionRatio = "${firstPage.width.toFloat() / firstPage.height.toFloat()}"
                    }
                    setImageURI(it)
                    setOnClickListener {
                        try {
                            openReader()
                        } catch (exception: Exception) {
                            Log.e(
                                "Failed to Open Reader",
                                "Error while opening reader cause : ${exception.message}"
                            )
                        }
                    }
                }
            },
            {
            }
        )
        findViewById<View>(R.id.buttonOpen).setOnClickListener {
            try {
                openReader()
            } catch (exception: Exception) {
                Log.e(
                    "Failed to Open Reader",
                    "Error while opening reader cause : ${exception.message}"
                )
            }
        }
    }

    private fun prepareProductRepo() {
        // Initialize the reader to open the contents
        productRepo = BookmarkProductRepository(readerSettings)
        productRepo.init(applicationContext, contentPath)
    }

    private fun openReader() {

        // For fragment
        /**
         val fragment = OneReaderFragment.newInstance(productRepo,readerSettings,readerListener)
         supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
         .commitNow()
         **/
        // for activity
        val activityOptionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                findViewById<ImageView>(R.id.coverImage),
                OneReaderActivity.SHARED_ELEMENT_TRANSITION_NAME
            )
        startActivity(
            OneReaderActivity.newIntent(
                context = this,
                readerSettings = readerSettings,
                productRepository = productRepo,
                readerListener = ORListener(productRepo = productRepo, "issueMid", this),
                searchProvider = CustomSearchProvider(),
                pageAdRepository = MiLibrisPageAdRepository(productRepo),
                sharedElementImageUrl = coverImageURL,
                sharedElementRatio = coverRatio
            ),
            activityOptionsCompat.toBundle()
        )
    }

    // Unpack archive
    // Unpack content if not done already
    private val contentPath: String
        get() {
            val contentFile = File(getExternalFilesDir(null), pdfName)

            // Unpack content if not done already
            if (!contentFile.isDirectory) {

                // Copy archive file to writable directory
                copySampleArchiveToDisk()

                // Unpack archive
                unpackArchive()
            }
            if (contentFile.isDirectory) {
                Log.i(TAG, "Content available at " + contentFile.absolutePath)
            }
            return contentFile.absolutePath
        }

    private fun copySampleArchiveToDisk() {
        val assetManager = assets
        val filename = "$pdfName.complete"
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = assetManager.open(filename)
            val outFile = File(getExternalFilesDir(null), filename)
            out = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to copy asset file: $filename", e)
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                }
            }
        }
    }

    private fun unpackArchive() {
        val foundationContext: com.milibris.foundation.FoundationContext =
            com.milibris.foundation.Foundation.createContext(applicationContext)
        try {
            val archive: com.milibris.foundation.CompleteArchive =
                com.milibris.foundation.CompleteArchive(
                    foundationContext,
                    assets.openFd("$pdfName.complete").createInputStream()
                )
            archive.unpackTo(File(getExternalFilesDir(null), pdfName))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
