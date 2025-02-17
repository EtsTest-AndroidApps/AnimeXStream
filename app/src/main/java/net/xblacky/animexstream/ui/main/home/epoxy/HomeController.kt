package net.xblacky.animexstream.ui.main.home.epoxy

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.Carousel.setDefaultGlobalSnapHelperFactory
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.epoxy.AnimeCommonModel_
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import net.xblacky.animexstream.utils.model.HomeScreenModel


class HomeController(var adapterCallbacks: EpoxyAdapterCallbacks) :
    TypedEpoxyController<ArrayList<HomeScreenModel>>() {


    override fun buildModels(data: ArrayList<HomeScreenModel>) {


        data.forEach { homeScreenModel ->

            AnimeMiniHeaderModel_()
                .id(homeScreenModel.typeValue)
                .typeName(homeScreenModel.type)
                .addIf(!homeScreenModel.animeList.isNullOrEmpty(), this)



            when (homeScreenModel.typeValue) {

                C.TYPE_MOVIE, C.TYPE_NEW_SEASON -> {
                    val movieModelList: ArrayList<AnimeCommonModel_> = ArrayList()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it

                        movieModelList.add(
                            AnimeCommonModel_()
                                .id(animeMetaModel.ID)
                                .clickListener { model, holder, _, _ ->
                                    adapterCallbacks.animeTitleClick(
                                        model = model.animeMetaModel(),
                                        sharedTitle = holder.animeTitle,
                                        sharedImage = holder.animeImageView
                                    )
                                }
                                .animeMetaModel(animeMetaModel)
                        )
                    }
                    setDefaultGlobalSnapHelperFactory(null)

                    CarouselModel_()
                        .id(homeScreenModel.hashCode())
                        .models(movieModelList)
                        .padding(Carousel.Padding.dp(20, 0, 20, 0, 20))
                        .addTo(this)

                }
                C.TYPE_POPULAR_ANIME -> {
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it

                        AnimePopularModel_()
                            .id(animeMetaModel.ID)
                            .clickListener { model, holder, _, _ ->
                                adapterCallbacks.animeTitleClick(
                                    model = model.animeMetaModel(),
                                    sharedTitle = holder.animeTitle,
                                    sharedImage = holder.animeImageView
                                )
                            }
                            .animeMetaModel(animeMetaModel)
                            .addTo(this)
                    }

                }
                else -> {
                    val recentModelList: ArrayList<AnimeSubDubModel2_> = ArrayList()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it
                        recentModelList.add(
                            AnimeSubDubModel2_()
                                .id(animeMetaModel.ID)
                                .clickListener { model, holder, clickedView, _ ->
                                    recentSubDubClick(
                                        model = model.animeMetaModel(),
                                        clickedView = clickedView,
                                        sharedTitle = holder.animeTitle,
                                        sharedImage = holder.animeImageView

                                    )
                                }
                                .animeMetaModel(animeMetaModel)
                        )
                    }
                    CarouselModel_()
                        .id(homeScreenModel.hashCode())
                        .models(recentModelList)
                        .padding(Carousel.Padding.dp(20, 0, 20, 0, 20))
                        .addTo(this)
                }
            }

        }

    }

    private fun recentSubDubClick(
        model: AnimeMetaModel,
        clickedView: View,
        sharedTitle: View,
        sharedImage: View
    ) {
        when (clickedView.id) {
            R.id.animeImage -> {
                adapterCallbacks.recentSubDubEpisodeClick(model = model)
            }
            R.id.animeTitle -> {
                adapterCallbacks.animeTitleClick(
                    model = model,
                    sharedTitle = sharedTitle,
                    sharedImage = sharedImage
                )
            }
        }

    }


    interface EpoxyAdapterCallbacks {
        fun recentSubDubEpisodeClick(model: AnimeMetaModel)
        fun animeTitleClick(model: AnimeMetaModel, sharedTitle: View, sharedImage: View)
    }

}