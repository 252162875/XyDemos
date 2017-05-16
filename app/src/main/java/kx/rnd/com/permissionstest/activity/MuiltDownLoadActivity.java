package kx.rnd.com.permissionstest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;

public class MuiltDownLoadActivity extends Activity {
    String defaultPath = "http://61.134.46.29/apk.r1.market.hiapk.com/data/upload/apkres/2016/10_27/16/com.motk_045303.apk";
    // 线程数
    private int threadCount = 3;
    private String path;

    @BindView(R.id.et_path)
    EditText mPathEt;
    @BindView(R.id.et_num)
    EditText mThreadNumEt;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.ll_content)
    LinearLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muilt_down_load);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_download)
    public void onClick() {
        download();
    }

    /**
     * 下载服务器文件
     */
    public void download() {
        path = mPathEt.getText().toString().trim().length() == 0 ? defaultPath : mPathEt.getText().toString().trim();
        String text = mThreadNumEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            threadCount = 3;
        } else {
            threadCount = Integer.valueOf(text);
            if (threadCount < 0) {
                Toast.makeText(this, "线程必须>0", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 移除ll里面已存在的所有view
        mContent.removeAllViews();

        // 有多少个线程，就添加多少个进度条
        for (int i = 0; i < threadCount; i++) {
            // 填充一个pb
            View v = View
                    .inflate(MuiltDownLoadActivity.this, R.layout.progressbar, null);
            // 给ll里面添加一个pb
            mContent.addView(v);
        }

        // 开启线程
        new Thread() {
            public void run() {
                downloadInThread();
            }
        }.start();
    }

    /**
     * 在子线程中进行网络通信
     */
    private void downloadInThread() {
        try {
            // 1. 创建Url对象
            URL url = new URL(path);
            // 2. 用Url对象打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 3. 设置参数
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            // 4. 获取返回状态
            int code = conn.getResponseCode();
            // 5. 获取服务器资源的大小
            if (code == 200) {
                // 服务器资源的大小
                int fileLength = conn.getContentLength();
                System.out.println("服务器资源的大小：" + fileLength);

                RandomAccessFile raf = new RandomAccessFile(getFileName(), "rw");
                // 创建和服务器资源一样大小的文件
                raf.setLength(fileLength);
                raf.close();

                // 拿到每个线程下载的区块大小
                int blockSize = fileLength / threadCount;
                for (int threadId = 0; threadId < threadCount; threadId++) {
                    // 开始下载的位置
                    int startIndex = threadId * blockSize;
                    int endIndex = (threadId + 1) * blockSize;
                    if (threadId == threadCount - 1) {
                        // 最后一个线程修正下载的结束位置
                        endIndex = fileLength - 1;
                    }
                    // 开启线程去下载对应线程的资源
                    new DownLoadThread(startIndex, endIndex, threadId).start();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建线程
     */
    private class DownLoadThread extends Thread {
        // 开始下载位置
        int startIndex;
        // 结束位置
        int endIndex;
        // 线程的Id
        int threadId;
        // 存储断点下载的位置
        int filePosition;
        //当前线程的进度条
        ProgressBar mPb;
        //当前线程的最大进度
        int totalSize;
        //当前线程下载的进度
        int progress;
        //理论开始下载的位置
        int fistStartIndex;

        public DownLoadThread(int startIndex, int endIndex, int threadId) {
            super();
            this.startIndex = startIndex;
            fistStartIndex = startIndex;

            this.endIndex = endIndex;
            totalSize = endIndex - startIndex;
            progress = startIndex;
            this.threadId = threadId;
            filePosition = startIndex;

            mPb = (ProgressBar) mContent.getChildAt(threadId);
            //设置线程最大的进度
            mPb.setMax(totalSize);
        }

        @Override
        public void run() {
            super.run();
            System.out.println("理论上：线程" + threadId + ":开始位置：" + startIndex
                    + "~结束位置:" + endIndex);
            // System.out.println("线程" + threadId + ":工作量"
            // + (endIndex - startIndex));
            try {
                // 读取上次一存储的下载的位置
                File threadTmpFile = new File(getTmpFileName(threadId));
                if (threadTmpFile.exists()) {
                    FileInputStream fis = new FileInputStream(threadTmpFile);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(fis));
                    String text = br.readLine();
                    filePosition = Integer.valueOf(text);
                    // 下一次开始下载文件的位置
                    startIndex = filePosition;
                    br.close();
                }
                System.out.println("实际上：线程" + threadId + ":开始位置：" + startIndex
                        + "~结束位置:" + endIndex);

                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(3000);
                // 重要，客户端请求服务器资源的区间
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
                        + endIndex);
                // 206部分资源请求成功
                int code = conn.getResponseCode();
                // System.out.println("code:"+code);
                if (code == 206) {
                    // 服务器返回的每个线程请求的区块资源
                    InputStream is = conn.getInputStream();
                    RandomAccessFile raf = new RandomAccessFile(getFileName(),
                            "rw");
                    // 重要，每个线程开始写文件的位置
                    raf.seek(startIndex);
                    byte[] buffer = new byte[1024 * 1024];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                        // 存储当前线程下载的位置
                        RandomAccessFile rfile = new RandomAccessFile(
                                getTmpFileName(threadId), "rwd");
                        // 重要， 相对于整个资源文件的位置
                        filePosition = filePosition + len;
                        progress = filePosition - fistStartIndex;
                        //给pb设置已经下载的
                        mPb.setProgress(progress);

                        String pos = String.valueOf(filePosition);
                        rfile.write(pos.getBytes());
                        rfile.close();
                    }
                    raf.close();
                    is.close();
                } else {
                    showToast("code:" + code);
                }
                showToast("线程" + threadId + ":下载完毕！");
                // 删除临时文件
                System.out.println(threadTmpFile.delete());
            } catch (Exception e) {
                e.printStackTrace();
                showToast("网络错误");
            }

        }

    }

    /**
     * 在任意线程弹土司
     *
     * @param text
     */
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            // 运行在主线里
            @Override
            public void run() {
                Toast.makeText(MuiltDownLoadActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取下载文件的路径
     *
     * @return
     */
    private String getFileName() {
        int index = path.lastIndexOf("/");
        return "/mnt/sdcard/" + path.substring(index);
    }

    /**
     * 进度临时文件存储的路径
     *
     * @param threadId
     * @return
     */
    private String getTmpFileName(int threadId) {
        return getFileName() + threadId + ".txt";
    }
}
