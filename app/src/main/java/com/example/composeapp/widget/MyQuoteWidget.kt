package com.example.composeapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.delay

class MyQuoteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val quote = fetchQuoteOfTheDay()
        provideContent {
            MyWidgetContent(quote = quote)
        }
    }

    private suspend fun fetchQuoteOfTheDay(): String {
        delay(1000)
        return "The best way to predict the future is to invent it."
    }
}

@Composable
fun MyWidgetContent(quote: String) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.DarkGray) // background 来自 androidx.glance.background
            .padding(16.dp), // padding 来自 androidx.glance.layout.padding
        verticalAlignment = Alignment.Vertical.CenterVertically, // Alignment 来自 androidx.glance.layout.Alignment
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        // Text 来自 androidx.glance.text.Text
        Text(
            text = "Quote of the Day",
            style = TextStyle(color = androidx.glance.unit.ColorProvider(Color.White)) // TextStyle 来自 androidx.glance.text
        )
        Text(
            text = quote,
            style = TextStyle(color = androidx.glance.unit.ColorProvider(Color.Yellow), fontSize = 16.sp)
        )
        // Button 来自 androidx.glance.Button
        Button(
            text = "Refresh",
            onClick = actionRunCallback<RefreshAction>()
        )
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        MyQuoteWidget().update(context, glanceId)
    }
}

class MyQuoteWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyQuoteWidget()
}