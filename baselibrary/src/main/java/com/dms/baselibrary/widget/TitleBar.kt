package com.dms.baselibrary.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.dms.baselibrary.R
import java.util.*

class TitleBar : ViewGroup, View.OnClickListener {
    private val mDefaultMainTextSize = 18
    private val mDefaultSubTextSize = 12
    private val mDefaultActionTextSize = 14
    private val mDefaultTitleBarHeight = 48

    private var mDefaultTitleBarBgColor: Int = 0
    private var mDefaultTitleBarTextColor: Int = 0
    private var mDefaultActionTextColor: Int = 0
    private val mStatusBarHeightResourceName = "status_bar_height"

    private var mLeftText: TextView? = null
    private var mRightLayout: LinearLayout? = null
    private var mCenterLayout: LinearLayout? = null
    private var mCenterText: TextView? = null
    private var mSubTitleText: TextView? = null
    private var mCustomCenterView: View? = null
    private var mDividerView: View? = null

    private var mImmersive: Boolean = false

    private var mScreenWidth: Int = 0
    private var mStatusBarHeight: Int = 0
    private var mActionPadding: Int = 0
    private var mOutPadding: Int = 0
    private var mActionTextColor: Int = 0
    private var mHeight: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight()
        }
        mActionPadding = dip2px(5)
        mOutPadding = dip2px(8)
        mHeight = dip2px(mDefaultTitleBarHeight)

        initView(context)
    }

    private fun initView(context: Context) {
        mLeftText = TextView(context)
        mCenterLayout = LinearLayout(context)
        mRightLayout = LinearLayout(context)
        mDividerView = View(context)

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mLeftText!!.textSize = mDefaultActionTextSize.toFloat()

        mDefaultTitleBarBgColor = ContextCompat.getColor(context, R.color.color_title_bar_bg)
        setBackgroundColor(mDefaultTitleBarBgColor)

        mDefaultTitleBarTextColor = ContextCompat.getColor(context, R.color.color_title_bar_text)
        mLeftText!!.setTextColor(mDefaultTitleBarTextColor)
        mLeftText!!.setSingleLine()
        mLeftText!!.gravity = Gravity.CENTER_VERTICAL
        mLeftText!!.setPadding(mOutPadding + mActionPadding, 0, mOutPadding, 0)

        mDefaultActionTextColor = ContextCompat.getColor(context, R.color.color_title_bar_action_text)

        mCenterText = TextView(context)
        mSubTitleText = TextView(context)
        mCenterLayout!!.addView(mCenterText)
        mCenterLayout!!.addView(mSubTitleText)

        mCenterLayout!!.gravity = Gravity.CENTER
        mCenterText!!.textSize = mDefaultMainTextSize.toFloat()
        mCenterText!!.typeface = Typeface.DEFAULT_BOLD
        mCenterText!!.setTextColor(mDefaultTitleBarTextColor)
        mCenterText!!.setSingleLine()
        mCenterText!!.gravity = Gravity.CENTER
        mCenterText!!.ellipsize = TextUtils.TruncateAt.END

        mSubTitleText!!.textSize = mDefaultSubTextSize.toFloat()
        mSubTitleText!!.setTextColor(mDefaultTitleBarTextColor)
        mSubTitleText!!.setSingleLine()
        mSubTitleText!!.gravity = Gravity.CENTER
        mSubTitleText!!.ellipsize = TextUtils.TruncateAt.END

        mRightLayout!!.setPadding(mOutPadding, 0, mOutPadding, 0)

        addView(mLeftText, layoutParams)
        addView(mCenterLayout)
        addView(mRightLayout, layoutParams)
        addView(mDividerView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1))
    }

    fun attachActivity(activity: AppCompatActivity) {
        val actionBar = activity.supportActionBar ?: return
        actionBar.elevation = 0f
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        val layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        actionBar.setCustomView(this, layoutParams)
        val parent = this.parent as Toolbar
        parent.setContentInsetsAbsolute(0, 0)

        setLeftImageResource(R.drawable.ic_back)
        setLeftClickListener(OnClickListener { activity.onBackPressed() })
    }

    fun setImmersive(immersive: Boolean) {
        mImmersive = immersive
        mStatusBarHeight = if (mImmersive) {
            getStatusBarHeight()
        } else {
            0
        }
    }

    fun setHeight(height: Int) {
        mHeight = height
        setMeasuredDimension(measuredWidth, mHeight)
    }

    fun setLeftImageResource(resId: Int) {
        mLeftText?.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    }

    fun setLeftClickListener(listener: OnClickListener) {
        mLeftText?.setOnClickListener(listener)
    }

    fun setLeftText(title: CharSequence) {
        mLeftText?.text = title
    }

    fun setLeftText(resId: Int) {
        mLeftText?.setText(resId)
    }

    fun setLeftTextSize(size: Float) {
        mLeftText?.textSize = size
    }

    fun setLeftTextColor(color: Int) {
        mLeftText?.setTextColor(color)
    }

    fun setLeftVisible(visible: Boolean) {
        mLeftText?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun getCenterTextView(): TextView? {
        return mCenterText
    }

    fun setTitle(resourceId: Int) {
        setTitle(resources.getString(resourceId))
    }

    fun setTitle(title: CharSequence) {
        var index = title.toString().indexOf("\n")
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length), LinearLayout.VERTICAL)
        } else {
            index = title.toString().indexOf("\t")
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length), LinearLayout.HORIZONTAL)
            } else {
                mCenterText!!.text = title
                mSubTitleText!!.visibility = View.GONE
            }
        }
    }

    private fun setTitle(title: CharSequence, subTitle: CharSequence, orientation: Int) {
        mCenterLayout!!.orientation = orientation
        mCenterText!!.text = title

        mSubTitleText!!.text = subTitle
        mSubTitleText!!.visibility = View.VISIBLE
    }

    fun setCenterClickListener(l: View.OnClickListener) {
        mCenterLayout!!.setOnClickListener(l)
    }

    fun setTitleColor(resourceId: Int) {
        mCenterText!!.setTextColor(resourceId)
    }

    fun setTitleSize(size: Float) {
        mCenterText!!.textSize = size
    }

    fun setTitleBackground(resourceId: Int) {
        mCenterText!!.setBackgroundResource(resourceId)
    }

    fun setSubTitleColor(resourceId: Int) {
        mSubTitleText!!.setTextColor(resourceId)
    }

    fun setSubTitleSize(size: Float) {
        mSubTitleText!!.textSize = size
    }

    fun setCustomTitle(titleView: View?) {
        if (titleView == null) {
            mCenterText!!.visibility = View.VISIBLE
            if (mCustomCenterView != null) {
                mCenterLayout!!.removeView(mCustomCenterView)
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout!!.removeView(mCustomCenterView)
            }
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            mCustomCenterView = titleView
            mCenterLayout!!.addView(titleView, layoutParams)
            mCenterText!!.visibility = View.GONE
        }
    }

    fun setDivider(drawable: Int) {
        mDividerView!!.setBackgroundResource(drawable)
    }

    fun setDividerColor(color: Int) {
        mDividerView!!.setBackgroundColor(color)
    }

    fun setDividerHeight(dividerHeight: Int) {
        mDividerView!!.layoutParams.height = dividerHeight
    }

    fun setActionTextColor(colorResId: Int) {
        mActionTextColor = ContextCompat.getColor(context, colorResId)
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    fun setOnTitleClickListener(listener: View.OnClickListener) {
        mCenterText!!.setOnClickListener(listener)
    }

    override fun onClick(view: View) {
        val tag = view.tag
        if (tag is Action) {
            tag.performAction(view)
        }
    }

    /**
     * Adds a list of [Action]s.
     *
     * @param actionList the actions to add
     */
    fun addActions(actionList: ActionList) {
        val actions = actionList.size
        for (i in 0 until actions) {
            addAction(actionList[i])
        }
    }

    /**
     * Adds a new [Action].
     *
     * @param action the action to add
     */
    public fun addAction(action: Action): View {
        val index = mRightLayout!!.childCount
        return addAction(action, index)
    }

    /**
     * Adds a new [Action] at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public fun addAction(action: Action, index: Int): View {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        val view = inflateAction(action)
        mRightLayout!!.addView(view, index, params)
        return view
    }

    /**
     * Removes all action views from this action bar
     */
    public fun removeAllActions() {
        mRightLayout!!.removeAllViews()
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public fun removeActionAt(index: Int) {
        mRightLayout!!.removeViewAt(index)
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public fun removeAction(action: Action) {
        val childCount = mRightLayout!!.childCount
        for (i in 0 until childCount) {
            val view = mRightLayout!!.getChildAt(i)
            if (view != null) {
                val tag = view.tag
                if (tag is Action && tag == action) {
                    mRightLayout!!.removeView(view)
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public fun getActionCount(): Int {
        return mRightLayout!!.childCount
    }

    /**
     * Inflates a [View] with the given [Action].
     *
     * @param action the action to inflate
     * @return a view
     */
    private fun inflateAction(action: Action): View {
        var view: View?
        val textStr = action.text
        if (TextUtils.isEmpty(textStr)) {
            val img = ImageView(context)
            img.setImageResource(action.drawable)
            view = img
        } else {
            val text = TextView(context)
            text.gravity = Gravity.CENTER
            text.text = action.text
            text.textSize = mDefaultActionTextSize.toFloat()
            text.setTextColor(mDefaultActionTextColor)
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor)
            }

            view = text
        }

        view.setPadding(mActionPadding, 0, mActionPadding, 0)
        view.tag = action
        view.setOnClickListener(this)

        return view
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecCopy = heightMeasureSpec
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpecCopy)
        val height: Int
        if (heightMode != View.MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight
            heightMeasureSpecCopy = View.MeasureSpec.makeMeasureSpec(mHeight, View.MeasureSpec.EXACTLY)
        } else {
            height = View.MeasureSpec.getSize(heightMeasureSpecCopy) + mStatusBarHeight
        }
        mScreenWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpecCopy)
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpecCopy)
        if (mLeftText!!.measuredWidth > mRightLayout!!.measuredWidth) {
            mCenterLayout!!.measure(
                    View.MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText!!.measuredWidth, View.MeasureSpec.EXACTLY), heightMeasureSpecCopy)
        } else {
            mCenterLayout!!.measure(
                    View.MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout!!.measuredWidth, View.MeasureSpec.EXACTLY), heightMeasureSpecCopy)
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpecCopy)
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mLeftText!!.layout(0, mStatusBarHeight, mLeftText!!.measuredWidth, mLeftText!!.measuredHeight + mStatusBarHeight)
        mRightLayout!!.layout(mScreenWidth - mRightLayout!!.measuredWidth, mStatusBarHeight,
                mScreenWidth, mRightLayout!!.measuredHeight + mStatusBarHeight)
        if (mLeftText!!.measuredWidth > mRightLayout!!.measuredWidth) {
            mCenterLayout!!.layout(mLeftText!!.measuredWidth, mStatusBarHeight,
                    mScreenWidth - mLeftText!!.measuredWidth, measuredHeight)
        } else {
            mCenterLayout!!.layout(mRightLayout!!.measuredWidth, mStatusBarHeight,
                    mScreenWidth - mRightLayout!!.measuredWidth, measuredHeight)
        }
        mDividerView!!.layout(0, measuredHeight - mDividerView!!.measuredHeight, measuredWidth, measuredHeight)
    }

    private fun dip2px(dpValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    private fun getStatusBarHeight(): Int {
        return getInternalDimensionSize(Resources.getSystem(), mStatusBarHeightResourceName)
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }


    /**
     * A [LinkedList] that holds a list of [Action]s.
     */
    class ActionList : LinkedList<Action>()

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    interface Action {
        val text: String

        val drawable: Int

        fun performAction(view: View)
    }

    abstract class ImageAction(override val drawable: Int) : Action {

        override val text: String
            get() = ""
    }

    abstract class TextAction(override val text: String) : Action {
        override val drawable: Int
            get() = -1

    }
}