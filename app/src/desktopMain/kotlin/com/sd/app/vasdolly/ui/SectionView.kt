package com.sd.app.vasdolly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Composable
fun SectionTitleView(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.clickable { onClick() }
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Text(text = title)
    }
}

@Composable
fun SectionItemView(
    modifier: Modifier = Modifier,
    selected: Boolean,
    name: String,
    onClickRemove: () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(16.dp)
                .background(Color.Red, CircleShape)
                .clickable { onClickRemove() }
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(5.dp))

        OutlinedButton(onClick = onClick) {
            Checkbox(checked = selected, onCheckedChange = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = name, lineHeight = 1.em)
        }
    }
}