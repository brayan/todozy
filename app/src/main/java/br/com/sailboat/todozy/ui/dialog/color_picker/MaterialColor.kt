package br.com.sailboat.todozy.ui.dialog.color_picker

import android.content.res.Resources
import br.com.sailboat.todozy.R

enum class MaterialColor(colorId: Int) {

    BLUE(R.color.md_blue_300),
    RED(R.color.md_red_300),
    TEAL(R.color.md_teal_300),
    AMBER(R.color.md_cyan_300),
    PINK(R.color.md_pink_300),
    INDIGO(R.color.md_indigo_300),
    BLUE_GREY(R.color.md_blue_grey_300),
    ORANGE(R.color.md_orange_300);

    companion object {
        fun getColor(index: Int): MaterialColor {
            for (color in values()) {
                if (color.ordinal == index) {
                    return color
                }
            }
            throw Resources.NotFoundException("Color not found for index $index")
        }
    }

}