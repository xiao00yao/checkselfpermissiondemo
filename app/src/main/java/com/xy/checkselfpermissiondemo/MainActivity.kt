package com.xy.checkselfpermissiondemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * android 6.0及更高版本需要权限申请
 * 开发环境如下:
 ** Android studio ->版本 4.0
 * project的gradle版本 -> distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
 */
class MainActivity : AppCompatActivity() {
    //需请求的权限  此处只是作为一个示例，请求多个权限的操作，实际操作中打电话不需要 READ_EXTERNAL_STORAGE 权限
    var permissions = arrayListOf(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //点击事件
    fun funCall(view: View) {
        //检查是否有权限
        val isHavePermission = cheachPermisssion()
        if (isHavePermission){ //有权限则直接拨打电话
            call()
            return
        }
        //没有权限时请求权限
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(),100)

    }
    //检查权限
    fun  cheachPermisssion():Boolean{
        /**
         * PackageManager.PERMISSION_GRANTED
         * PackageManager.PERMISSION_DENIED
         */
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this,it)==PackageManager.PERMISSION_DENIED){
                return false
            }
        }
        return true
    }
    //拨打电话
    fun call(){
        var intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + 12345678945))
        this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode==100){
            var canCall = true
            for (i in permissions.indices){
                if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                    canCall = false
                }
            }
            if (canCall){ //全部权限都允许
                call()
            }else{
                //此处你也可以添加一个弹窗 让用户跳转到设置界面打开相应的权限，此处我只做了一个简单的Toast 提示
                Toast.makeText(this,"请到设置中打开相应的权限，否侧影响正常操作",Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}