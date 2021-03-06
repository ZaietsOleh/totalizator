package com.kvad.totalizator.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kvad.totalizator.chat.ui.UserMessageUi

class MessageDiffUtil : DiffUtil.ItemCallback<UserMessageUi>() {
    override fun areItemsTheSame(oldItem: UserMessageUi, newItem: UserMessageUi) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserMessageUi, newItem: UserMessageUi) =
        oldItem == newItem
}
