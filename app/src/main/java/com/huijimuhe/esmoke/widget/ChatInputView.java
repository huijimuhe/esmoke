package com.huijimuhe.esmoke.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.huijimuhe.esmoke.R;


/**
 * Created by Huijimuhe on 2016/3/18.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.widget
 * please enjoy the day and night when you work hard on this.
 */
public class ChatInputView extends LinearLayout {
    private EditText editText;
    private View buttonSend;
    private ChatInputMenuListener listener;

    public ChatInputView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_chat_input, this, true);
        editText = (EditText) findViewById(R.id.et_sendmessage);
        buttonSend = findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                editText.setText("");
                listener.onSendMessage(s);
            }
        });
    }

    public void setChatInputMenuListener(ChatInputMenuListener listener) {
        this.listener = listener;
    }

    public interface ChatInputMenuListener {
        /**
         * 发送消息按钮点击
         *
         * @param content 文本内容
         */
        void onSendMessage(String content);

    }
}
