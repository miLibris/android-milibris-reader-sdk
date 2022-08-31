# # MiLibrisReaderSDK
MiLibrisReaderSDK is the new miLibris reading SDK (previously called MLPDFReaderSDK). It includes the MLFoundation library which allows unpacking miLibris contents.

### Prerequisites

MiLibrisReaderSDK developed in Kotlin requires Android 5 or higher, and uses glide to load images as external library and other androidX libraries like ViewModel and appCompact and others.

Every app using the SDK must be configured with a licence key provided by miLibris. A licence key cannot be used in more than one application.

### Setup

The library is available on our maven and can be added to your Android project as follow:
```groovy
repositories {  
	google()  
    mavenCentral()  
    maven { url 'https://maven-android-sdk.milibris.net/' }  
}

dependencies {
	api("co.cafeyn:one-reader:0.18.1") {  
	//If you ever have conflict with the version used in our libary add this line
	  exclude group: "androidx.lifecycle"  
	}  
	api("com.milibris:milibris-reader:0.18.1")
}
```

In order for the SDK to work properly you need to add the licence key provided in your manifest as below
```xml
<application 
 ....
 >
	<meta-data
	    android:name="com.milibris.pdfreader.licencekey"
	    android:value="YOUR_LICENCE_KEY" />
</application>
```


### Reader Sample APP

This is a sample using the new Milibris reader including an example of the its implementation.
You can download the library using maven as follow:


# Implementation

In order to read a content, your application will likely implement the following steps:
1. Download a complete archive (with the *.complete extension) from the miLibris
   platform.
2. Unpack the archive using MLFoundation
3. Launch Reader to read the unpacked contents

**1. Unpack a complete archive with MLFoundation**

A complete archive can be easily unpacked with the MLFoundation library utilities (see
example below, extracting a sample.complete file in Android assets).
```kotlin
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
```

**2. Read unpacked contents**

Once unpacked, you can open the content by starting OneReaderActivity:
```kotlin
// Initialize the reader to open the contents  
val productRepo = XmlPdfReaderDataSource(readerSettings)  
productRepo.init(applicationContext, contentPath)
startActivity(OneReaderActivity.newIntent(  
    this,  
  ReaderSettings(),  
  productRepo,  
  ORListener(dataSource = productRepo, "issueMid", this),  
  coverImageURL,  
  coverRatio  
))
```
We are providing a ReaderSettings class where you can customize the reader as you please to enable or disable some features.
```kotlin 
val readerSettings = ReaderSettings().apply {  
  isFaceCropEnabled = true  
  debugBoxes = true  
  targetPageNumber = targetPage  
  textToSpeechEnabled = true  
  shareEnabled = true  
  enableSummaryImages = true  
  logo = R.drawable.milibris  
}
```
If you want to track user events on reader you need to implement create your own class that implements The ReaderLister :
```kotlin
  
class ORListener : ReaderListener {  
	override fun onCloseButtonClickListener() {  }  
    override fun onIssueRead() {  }  
    override fun onIssuePageRead(pageNumber: Int, isCalledFromArticles: Boolean) {  }  
    override fun onSummaryOpened(isOpenedFromArticles: Boolean) {  }
	....
```
**3. Sample project**

A sample project is provided to help you implement the reader integration. It contains an example to unpack a complete archive and to open if with PdfReader class.

If your miLibris content has articles, you can implement your own sharing solution by adding your own code in onShareClicked() just like shown in this sample in the class ORListener
