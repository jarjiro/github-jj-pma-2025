package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Helper.ManagmentFavorite
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.domain.ItemsModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentFavorite: ManagmentFavorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        managmentFavorite = ManagmentFavorite(this)

        bundle()
        initVariantSelection()
        setupFavoriteBtn()
        setupFullscreenImage()
        setupShareBtn()
    }

    private fun setupShareBtn() {
        binding.shareBtn.setOnClickListener {
            val shareText = StringBuilder()
            shareText.append("${item.title}\n\n")
            shareText.append("Cena: ${PriceFormatter.format(item.price)}\n\n")
            shareText.append("Popis:\n${item.description}\n\n")
            shareText.append("Sdíleno z 3D Tisk E-shopu")

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, item.title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString())
            startActivity(Intent.createChooser(shareIntent, "Sdílet produkt přes"))
        }
    }

    private fun setupFullscreenImage() {
        binding.picMain.setOnClickListener {
            val intent = Intent(this, FullscreenImageActivity::class.java)
            intent.putExtra("imageUrl", item.picUrl[0])
            startActivity(intent)
        }
    }

    private fun setupFavoriteBtn() {
        if (managmentFavorite.isFavorite(item)) {
            binding.favBtn.setImageResource(R.drawable.ic_heart_full)
            binding.favBtn.setColorFilter(getColor(R.color.orange))
        } else {
            binding.favBtn.setImageResource(R.drawable.ic_heart_outline)
            binding.favBtn.setColorFilter(getColor(R.color.white))
        }

        binding.favBtn.setOnClickListener {
            managmentFavorite.insertItem(item)
            setupFavoriteBtn()
        }
    }

    private fun initVariantSelection() {
        if (item.categoryId == "0") {
            binding.variantLayout.visibility = View.VISIBLE
            item.selectedVariant = ""
            binding.smallBtn.setBackgroundResource(0)
            binding.largeBtn.setBackgroundResource(0)
        } else {
            binding.variantLayout.visibility = View.GONE
            item.selectedVariant = ""
        }

        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.orange_full_corner_bg)
                largeBtn.setBackgroundResource(0)
                item.selectedVariant = "Sestaveno"
            }

            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.orange_full_corner_bg)
                item.selectedVariant = "Stavebnice"
            }
        }
    }

    private fun bundle() {
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel
            item.numberInCart = 1

            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = PriceFormatter.format(item.price)
            ratingTxt.text = item.rating.toString()

            numberInCartTxt.setText(item.numberInCart.toString())

            numberInCartTxt.doAfterTextChanged {
                val input = it.toString()
                if (input.isNotEmpty()) {
                    val count = input.toInt()
                    if (count > 0) {
                        item.numberInCart = count
                    }
                }
            }

            addToCartBtn.setOnClickListener {
                if (item.categoryId == "0" && item.selectedVariant.isEmpty()) {
                    Toast.makeText(this@DetailActivity, "Prosím zvolte provedení (Sestaveno/Stavebnice)", Toast.LENGTH_SHORT).show()
                } else {
                    managmentCart.insertItems(item)
                }
            }
            
            backBtn.setOnClickListener {
                finish()
            }

            plusBtn.setOnClickListener {
                item.numberInCart++
                numberInCartTxt.setText(item.numberInCart.toString())
            }

            minusBtn.setOnClickListener {
                if (item.numberInCart > 1) {
                    item.numberInCart--
                    numberInCartTxt.setText(item.numberInCart.toString())
                }
            }
        }
    }
}
