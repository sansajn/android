# Android

Projekt obsahuje kolekciu programou predvádzajúcich základnu funkcionalitu android-u.


## kotlin:ukážky

Ukážky predvádzajúce android funkcionalitu v jazyku kotlin, dostupné v adresáry `kotlin`.

**GridView** : ukážka použitia `GridView` komponenty.

![](images/GridView.png "GridView sample")

## ndk:ukážky

Ukážky predvádzajúce funkcionalitu JNI, dostupné v adresáry `jni`.

**HelloJni** : základná ukážka, volanie bezparametrickej funkcie implementovanej v jni.

**CMake** : ukážka použitia cmake (a standalone toolchain) na miesto štandardného ndk-build. Standalone toolchain je nutné najprv vygenerovať, pozri `android_tips.txt`.

**Cpp11** : jednoduchá ukážka demonštrujúca použitie c++11 kompatibilného kódu.

**LoadRes** : cez jni prečíta zdroje (assets) zbalené v apk súbore

**TriangleGles2** : trojuholník vyrenderovaný pomocou opengl es 2

ndk/LoadingPng : čítanie png pomocou libpng (staticky zlinkovana a predinštalovana v toolchaine)

ndk/LoadingJpeg : čítanie jpeg pomocoi libjpeg

ndk/ReadFile : čítanie s file systemu s jni

ndk/Shapes : generovanie tvarou pomocou kničnice shapes

ndk/Earth : sustava zem, mesiac slnko s osvetlenim a texturami

ndk/ImageMagick : galeria pomocou image-magick-u

ndk/OpenAL : prehrá zvuk pomocou knižnice openal

ndk/WaveformPlay : prehrá zvuk vo formáte wav (waveform)

ndk/VorbisPlay : prehrá zvuk vo formáte ogg/vorbis

ndk/Animation : gpu anymácia modelu (skinning)

ndk/AntApp

ndk/GradleApp

ndk/Bullet : streľba kociek voči stene v bullet-e

ndk/CustomNative


## java:ukážky (muzeum)

Lifecycle

OpenGLWindow

Service

TouchDemo1 : ukážka použitia dotykového displeja

MultiTouchDemo1 : ukážka použitia viacdotikového displeja

Galacticon : advanced RecyclerView sample

RecyclerView : most simple RecyclerView sample

RecyclerViewGallery : RecyclerView with image sample

**ndk/TwoSharedLibraries**: Použitie knižnice zo závislosťou na inej dynamickej knižnici.

Pri loadovaní prvej *native-lib* knižnice systém zistí jej závislosť na druhej knižnici *b* a sám ju natiahne (netreba volať `System.LoadLibrary()`).


**ndk/TwoSharedLibrariesInKotlin**: Použitie knižnice zo závislosťou na inej dynamickej knižnici v Kotline. Pozri popis `ndk/TwoSharedLibraries`.
