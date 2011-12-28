package com.itjiaozi.iris.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Observer;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.Constant;
import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.ai.BaseTheAi;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.ai.TheAiManager;

public class IFlySpeechUtil {
    public static void startRecoginze(final ETheAiType eTheAiType, final Observer result) {
        final BaseTheAi bta = TheAiManager.getInstance().getTheAi(eTheAiType);

        if (bta.getIsNeedUploadKeys()) {
            UploadDialog uploadDialog = new UploadDialog(TheApplication.getInstance().getCurrentActivity(), "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("数据上传错误：" + error);
                    }
                }

                @Override
                public void onDataUploaded(String arg0, String grammarID) {
                    bta.setGrammarID(grammarID);
                    bta.setIsNeedUploadKeys(false);
                    startRecoginze(eTheAiType, null);
                }
            });
            String keys = bta.getKeysString();
            try {
                uploadDialog.setContent(keys.getBytes("UTF-8"), "dtt=keylist", bta.getGrammarName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            uploadDialog.show();
        } else {
            RecognizerDialog rd = new RecognizerDialog(TheApplication.getInstance().getCurrentActivity(), "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            rd.setListener(new RecognizerDialogListener() {

                StringBuilder sb = new StringBuilder();

                @Override
                public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
                    if (null != results && results.size() > 0) {
                        sb.append(results.get(0).text);
                    }
                    if (isLast) {
                        String ret = sb.toString();
                        if (null != result) {
                            ToastUtil.showToast("" + sb);
                            result.update(null, ret);
                        }
                        sb = new StringBuilder();
                    }
                }

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("识别错误：" + error);
                    }
                }
            });
            String engine = null;
            String grammarID = bta.getGrammarID();
            if (null == grammarID) {
                engine = "sms";
            }
            rd.setEngine(engine, null, bta.getGrammarID());
            rd.show();
        }
    }
}
