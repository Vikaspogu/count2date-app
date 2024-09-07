package com.vikaspogu.count2date.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentSize
import com.vikaspogu.count2date.MainActivity
import com.vikaspogu.count2date.R
import com.vikaspogu.count2date.data.model.Event
import com.vikaspogu.count2date.data.repository.EventRepository
import com.vikaspogu.count2date.ui.home.formatDate
import com.vikaspogu.count2date.ui.utils.GlanceText
import com.vikaspogu.count2date.ui.utils.getDaysLeft
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class DetailsWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EventProviderEntryPoint {
        fun eventRepository(): EventRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val appContext = context.applicationContext
        val eventEntryPoint =
            EntryPointAccessors.fromApplication(appContext, EventProviderEntryPoint::class.java)
        val eventRepository = eventEntryPoint.eventRepository()

        provideContent {
            val events by eventRepository.getAllEvents().collectAsState(initial = emptyList())
            GlanceTheme {
                Scaffold(
                    modifier = GlanceModifier.fillMaxSize(),
                    backgroundColor = GlanceTheme.colors.widgetBackground,
                ) {
                    LazyColumn(modifier = GlanceModifier.padding(top = 20.dp)) {
                        item { DetailsCard(events) }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsCard(events: List<Event>) {
    Column {
        events.forEach { event ->
            Row(
                modifier = GlanceModifier.fillMaxSize().clickable(actionStartActivity(MainActivity::class.java)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    GlanceText(
                        modifier = GlanceModifier.wrapContentSize(),
                        text = event.description,
                        color = Color.Black,
                        letterSpacing = 0.03f.sp,
                        font = R.font.rubik,
                        fontSize = 16.sp
                    )
                    GlanceText(
                        modifier = GlanceModifier.wrapContentSize(),
                        text = event.eventDate.formatDate(),
                        color = Color.Black,
                        letterSpacing = 0.03f.sp,
                        font = R.font.rubik,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = GlanceModifier.width(5.dp))
                Column {
                    GlanceText(
                        modifier = GlanceModifier.wrapContentSize(),
                        text = getDaysLeft(event.eventDate).toString(),
                        color = Color.Magenta,
                        letterSpacing = 0.03f.sp,
                        font = R.font.rubik,
                        fontSize = 18.sp
                    )
                    GlanceText(
                        modifier = GlanceModifier.wrapContentSize(),
                        text = "days",
                        color = Color.Black,
                        letterSpacing = 0.03f.sp,
                        font = R.font.rubik,
                        fontSize = 16.sp
                    )
                }
            }
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .height(1.dp)
                    .background(Color.Gray)
            ) {}
        }
    }

}

class Count2DateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DetailsWidget()
}