# Android

Repozitár obsahuje kolekciu programou/ukážok predvádzajúcich použitie jednotlivých komponent systému Android.

## kotlin:ukážky

Ukážky predvádzajúce android funkcionalitu v jazyku kotlin, dostupné v adresáry `kotlin`.

**GridView** : ukážka použitia `GridView` komponenty.

**ListView** : ukážka použitia `ListView` komponenty.

**TableLayout** : ukážka použitia dynamicky naplnenéj `TableLayout` komponenty. `TableLayout` je možné použiť pri zobrazovaní dát vo forme tabuľky (zdá sa, že android k tomuto účelu neimplementuje žiadnu štandardnú komponentu).

![](images/grid_view_small.png "GridView sample screenshot.") ![](images/list_view_small.png "ListView sample screenshot.") ![](images/table_layout_small.png "TableLayout sample screenshot.")



**ImageView** : ukážka práce s `ImageView` komponentou.

**SQLiteDatabase** : ukážka práce s SQLite databázou, aplykácia predvádza, ako spustiť aktyvitu a získať z nej dáta zadané užívateľom, ako vytvoriť menu, alebo aj ako použiť ListView.

![](images/image_view_small.png "ImageView sample screenshot.") ![](images/sqlite_database_small.png "SQLiteDatabase main activity sample screenshot.") ![](images/sqlite_database_edit_activity_small.png "SQLiteDatabase edit activity sample screenshot.")



**AndroidPlot** : ukážka renderovania grafou (xy plot) pomocou knižnice androidplot

**BarPlot** : ukážka renderovania bar grafu knižnicou androidplot.

**FXPlot**: vyzualizácia funkcie y=x^2-13 v android plot knižnici (negatívne hodnoty, grid, popisky)

![](images/simplexy_plot.png "AndroidPlot sample screenshot.") ![](images/barplot.png "BarPlot sample screenshot.") ![](images/fxplot.png "fx plot sample screenshot.")


**CombinedPlot** : ďalšie použitie knižnice androidplot 

![](images/combined_plot.png "fx plot sample screenshot.")



**TabLayout** : použitie tabou a fragmentou v apke

**JeromqPush** : ukážka použitia push-pull ZMQ soketou

**jeromq/RouterDealerSample** : ukážka použitia router/dealer ZMQ soketou

**NavigationDrawer**: ukážka použitia `DrawerLayout` a `NavigationView` v ktorej sú jednotlivé volby (home, gallery, slideshow, ...) implementované ako fragmenty (`Fragment`). 

![](images/tablayout.png "TabLayout sample screenshot.") ![](images/navigation_drawer.png "NavigationDrawer sample screenshot.")



**Preferences** : ukážka dizajnu nastavení pomocou `PreferenceFragmentCompat` s fungujúcim summary.

**EmptyListPlaceholder**: ukážka ako na placeholder view v prípade prázdneho ListView, tiež ukazuje ako použiť FrameLayout.

**RecyclerView**: použitie `RecyclerView` a `CardView` komponenty.

![](images/preferences.png "Preferences sample screenshot.") ![](images/empty_list_placeholder.png "EmptyListPlaceholder sample screenshot.") ![](images/recyclerview.png "RecyclerView sample screenshot.")



**UiThreadAccessWithoutActivity**: ako spúštať kód v UI vlákne bez aktivity (`Activity.runOnUiThread()`).

**HorizontalRecyclerView**: horizontálny `RecyclerView`.

**RecyclerViewDragandDrop**: ako na drag&drop u `RecyclerView`.

**RecyclerViewUpdate**: ukážka ako updateovať `RecyclerView` pri zmene zobrazovaných dát.

![](images/horizontal_recyclerview.png "Horizontal Recycler View sample screenshot") ![](images/recyclerview_draganddrop.png "Recycler View Drag&Drop sample screenshot") ![](images/recyclerview_update.png "Recycler View update sample screenshot")



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

**BarPlot** : ukážka renderovania bar grafu knižnicou androidplot.

**ndk/TwoSharedLibraries**: Použitie knižnice zo závislosťou na inej dynamickej knižnici.

Pri loadovaní prvej *native-lib* knižnice systém zistí jej závislosť na druhej knižnici *b* a sám ju natiahne (netreba volať `System.LoadLibrary()`).


**ndk/TwoSharedLibrariesInKotlin**: Použitie knižnice zo závislosťou na inej dynamickej knižnici v Kotline. Pozri popis `ndk/TwoSharedLibraries`.


