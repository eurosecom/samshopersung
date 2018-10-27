package com.eusecom.samshopersung.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import com.eusecom.samshopersung.CategoryKt


class ActivityLaunchHelper {

    companion object {

        const val EXTRA_EDIT = "EDIT"

        private const val URL_BASE = "https://www.edcom.sk"
        private const val URL_SIGNIN = "$URL_BASE/signin"
        private const val URL_CATEGORIES = "$URL_BASE/categories"
        private const val URL_QUIZ_BASE = "$URL_BASE/quiz/"

        //i added to start QuizActivity from MainShopperActivity by URL
        fun launchQuiz(activity: Activity, edit: Boolean = false) {
            ActivityCompat.startActivity(activity,
                    quizNewIntent(activity, edit),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
        }
        fun quizNewIntent(context: Context? = null, edit: Boolean = false): Intent =
                baseIntent(URL_QUIZ_BASE, context).putExtra(EXTRA_EDIT, edit)

        //end i added to start QuizActivity from MainShopperActivity by URL

        fun launchCategorySelection(activity: Activity, options: ActivityOptionsCompat? = null) {
            val starter = categorySelectionIntent(activity)
            if (options == null) {
                activity.startActivity(starter)
            } else {
                ActivityCompat.startActivity(activity, starter, options.toBundle())
            }
        }

        fun categorySelectionIntent(context: Context? = null) = baseIntent(URL_CATEGORIES, context)

        fun quizIntent(category: CategoryKt, context: Context? = null) =
                baseIntent("$URL_QUIZ_BASE${category.cat}", context)

        private fun baseIntent(url: String, context: Context? = null): Intent {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addCategory(Intent.CATEGORY_BROWSABLE)
            if (context != null) {
                intent.`package` = context.packageName
            }
            return intent
        }
    }
}