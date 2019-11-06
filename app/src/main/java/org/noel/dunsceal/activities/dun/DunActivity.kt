package org.noel.dunsceal.activities.dun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.dun_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.noel.dunsceal.R
import org.noel.dunsceal.helpers.readImage
import org.noel.dunsceal.helpers.readImageFromPath
import org.noel.dunsceal.helpers.showImagePicker
import org.noel.dunsceal.main.DunScealApp
import org.noel.dunsceal.model.Location
import org.noel.dunsceal.model.DunModel

class DunActivity : AppCompatActivity(), AnkoLogger {

  var dun = DunModel()
  lateinit var app: DunScealApp
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  var location = Location(52.245696, -7.139102, 15f)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dun_activity)
    if (toolbarAdd != null) {
      toolbarAdd.title = title
      setSupportActionBar(toolbarAdd)
    }
    info("Dun Activity started..")

    app = application as DunScealApp
    var edit = false

    if (intent.hasExtra("dun_edit")) {
      edit = true
      dun = intent.extras?.getParcelable<DunModel>("dun_edit")!!
      dunTitle.setText(dun.name)
      description.setText(dun.description)
      dunImage.setImageBitmap(readImageFromPath(this, dun.image))
      if (dun.image != null) {
        chooseImage.setText(R.string.change_dun_image)
      }
      btnAdd.setText(R.string.save_dun)
    }

    btnAdd.setOnClickListener {
      dun.name = dunTitle.text.toString()
      dun.description = description.text.toString()
      if (dun.name.isEmpty()) {
        toast(R.string.enter_dun_title)
      } else {
        if (edit) {
          app.dunStore.update(dun.copy())
        } else {
          app.dunStore.create(dun.copy())
        }
      }
      info("add Button Pressed: $dunTitle")
      setResult(RESULT_OK)
      finish()
    }

    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    dunLocation.setOnClickListener {
      startActivityForResult(intentFor<DunMapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_dun, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          dun.image = data.getData().toString()
          dunImage.setImageBitmap(readImage(this, resultCode, data))
          chooseImage.setText(R.string.change_dun_image)
        }
      }
      LOCATION_REQUEST -> {
        if (data != null) {
          location = data.extras?.getParcelable<Location>("location")!!
        }
      }
    }
  }
}