package com.ns.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.netoperation.config.model.ArticleTextColor;
import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.view.FontCache;

public class SectionTextView extends AppCompatTextView {

    public SectionTextView(Context context) {
        super(context);
        init(context, null);
    }

    public SectionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SectionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            int textType;
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextView);

            String mFontPath = typedArray.getString(R.styleable.TextView_font_path);
            if (typedArray.hasValue(R.styleable.TextView_textType)) {
                textType = typedArray.getInt(R.styleable.TextView_textType, 0);
            } else {
                textType = -1;
            }

            if (mFontPath == null || mFontPath.isEmpty()) {
                mFontPath = getResources().getString(R.string.THP_FiraSans_Regular);
            }

            applyCustomFont(context, mFontPath);

            typedArray.recycle();

            final TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();

            if(tableConfiguration == null) {
                return;
            }

            final ArticleTextColor articleTextColor = tableConfiguration.getAppTheme().getArticleText();

            // title
            if(textType == 5) {
                if(BaseAcitivityTHP.sIsDayTheme) {
                    setTextColor(Color.parseColor(articleTextColor.getLight().getTitle()));
                }
                else {
                    setTextColor(Color.parseColor(articleTextColor.getDark().getTitle()));
                }
            }
            // time
            else if(textType == 6) {
                if(BaseAcitivityTHP.sIsDayTheme) {
                    setTextColor(Color.parseColor(articleTextColor.getLight().getTime()));
                }
                else {
                    setTextColor(Color.parseColor(articleTextColor.getDark().getTime()));
                }
            }
            // author
            else if(textType == 7) {
                if(BaseAcitivityTHP.sIsDayTheme) {
                    setTextColor(Color.parseColor(articleTextColor.getLight().getAuthor()));
                }
                else {
                    setTextColor(Color.parseColor(articleTextColor.getDark().getAuthor()));
                }
            }
            // section
            else if(textType == 8) {
                if(BaseAcitivityTHP.sIsDayTheme) {
                    setTextColor(Color.parseColor(articleTextColor.getLight().getSection()));
                }
                else {
                    setTextColor(Color.parseColor(articleTextColor.getDark().getSection()));
                }
            }
            // detail
            else if(textType == 9) {
                if(BaseAcitivityTHP.sIsDayTheme) {
                    setTextColor(Color.parseColor(articleTextColor.getLight().getDetail()));
                }
                else {
                    setTextColor(Color.parseColor(articleTextColor.getDark().getDetail()));
                }
            }

        }




    }

    private void applyCustomFont(Context context, String fontName) {
        Typeface customFont = FontCache.getTypeface(fontName, context);
        setTypeface(customFont);
    }



}
