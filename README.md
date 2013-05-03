# SampleTodoAndroid

Androidの勉強用のサンプルアプリです。

まだ全然TODOになってないです。

## ビルド

Maven v3.0.3+とAndroid SDKが必要です、また環境変数ANDROID_HOMEにSDKのパスを指定する必要があります:

```bash
export ANDROID_HOME=/opt/tools/android-sdk
```

ビルド自体は以下コマンドで実行します

```bash
$ mvn clean package
```

Eclipseから利用する場合にはMaven Integration for Eclipseが必要です。

導入方法は以下ページが詳しいです。

http://d.hatena.ne.jp/esmasui/20130301/1362115555

## 参考サイト

* [github/android](https://github.com/github/android)

* [Fragmentでタブを実現する](http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/)

