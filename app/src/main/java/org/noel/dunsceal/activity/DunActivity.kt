package org.noel.dunsceal.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_edit.*
import kotlinx.android.synthetic.main.content_date.*
import org.jetbrains.anko.*
import org.noel.dunsceal.R
import org.noel.dunsceal.helpers.readImage
import org.noel.dunsceal.helpers.readImageFromPath
import org.noel.dunsceal.helpers.showImagePicker
import org.noel.dunsceal.main.DunScealApp
import org.noel.dunsceal.models.Location
import org.noel.dunsceal.models.DunModel
import java.text.SimpleDateFormat
import java.util.*

class DunActivity : AppCompatActivity(), AnkoLogger {

  var dun = DunModel()
  lateinit var app: DunScealApp

  // menu objects
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  var totalCompleted = 0

  // Button objects
  var button_date: Button? = null
  var switch_visit: Switch? = null

  // Other objects
  var button_text: String = ""
  var cal = Calendar.getInstance()
  val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_edit)
    toolbarAdd.title = title
    setSupportActionBar(toolbarAdd)

    app = application as DunScealApp
    progressBar.max = 10000
    var edit = false

    // get the references from layout file
    button_text = this.button_date_picker?.text.toString()
    button_date = this.button_date_picker
    switch_visit = this.visit_status

    // create an OnDateSetListener
    val dateSetListener =
      DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        button_date?.text = sdf.format(cal.time)
      }

    // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
    button_date!!.setOnClickListener {
      DatePickerDialog(
        this@DunActivity,
        dateSetListener,
        // set DatePickerDialog to point to today's date when it loads up
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
      ).show()
    }

    switch_visit!!.setOnClickListener {
      when (switch_visit!!.isChecked) {
        true -> { switch_visit?.text = "Visited"
          Toast.makeText(applicationContext, R.string.isVisited, Toast.LENGTH_SHORT).show() }
        false -> { switch_visit?.text = "Not Visited"
          Toast.makeText(applicationContext, R.string.notVisited, Toast.LENGTH_SHORT).show() }
      }
    }

    if (intent.hasExtra("dun_edit")) {
      edit = true
      dun = intent.extras?.getParcelable<DunModel>("dun_edit")!!
      dunTitle.setText(dun.title)
      description.setText(dun.description)
      dunImage.setImageBitmap(readImageFromPath(this, dun.image))
      if (dun.image != null) {
        chooseImage.setText(R.string.change_dun_image)
      }
      btnAdd.setText(R.string.save_dun)
    }

    btnAdd.setOnClickListener() {
      dun.title = dunTitle.text.toString()
      dun.description = description.text.toString()
      if (dun.title.isEmpty()) {
        toast(R.string.enter_dun_title)
      } else {
        if (edit) {
          app.duns.update(dun.copy())
        } else {
          app.duns.create(dun.copy())
        }
      }
      info("add Button Pressed: $dunTitle")
      setResult(AppCompatActivity.RESULT_OK)
      finish()
    }

    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    dunLocation.setOnClickListener {
      val location = Location(52.245696, -7.139102, 15f)
      if (dun.zoom != 0f) {
        location.lat = dun.lat
        location.lng = dun.lng
        location.zoom = dun.zoom
      }
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    /** fab.setOnClickListener {
      if (visit_status.isChecked) visit_status.text = "Visited"
      else visit_status.text = "Not visited"
      if (totalCompleted >= progressBar.max)
        toast("All Duns visited")
      else {
        totalAmount.text = "$${totalCompleted}"
        progressBar.progress = totalCompleted
        app.duns.create(
          DunModel(
            isComplete = visit_status.isChecked,
            date = cal.getTime()
          )
        )
      }
    } **/
  }

  override fun onResume() {
    super.onResume()
    totalCompleted = app.duns.findAll().sumBy { it.isComplete }
    progressBar.progress = totalCompleted
    totalAmount.text = "$$totalCompleted"
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_dun_cancel, menu)
    menuInflater.inflate(R.menu.menu_dun_list, menu)
    menuInflater.inflate(R.menu.menu_dun_users, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.action_dun_add -> startActivityForResult<DunActivity>(0)
      R.id.action_dun_list-> startActivity<DunListActivity>()
      R.id.action_dun_users -> startActivity<DunUserActivity>()
      else -> finish()
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
          val location = data.extras?.getParcelable<Location>("location")!!
          dun.lat = location.lat
          dun.lng = location.lng
          dun.zoom = location.zoom
        }
      }
    }
  }

  fun alertDemo() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Androidly Alert")
    builder.setMessage("This is a message")

    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
      Toast.makeText(
        applicationContext,
        android.R.string.yes, Toast.LENGTH_SHORT
      ).show()
    }

    builder.setNegativeButton(android.R.string.no) { dialog, which ->
      Toast.makeText(
        applicationContext,
        android.R.string.no, Toast.LENGTH_SHORT
      ).show()
    }

    builder.setNeutralButton("Maybe") { dialog, which ->
      Toast.makeText(
        applicationContext,
        "Maybe", Toast.LENGTH_SHORT
      ).show()
    }
    builder.show()

  }

  fun alertAnkoDemo() {
    alert("This is a message", "Androidly Alert") {
      yesButton { toast(android.R.string.yes) }
      noButton { toast(android.R.string.no) }
      neutralPressed("Maybe") { toast("Maybe") }
    }.show()
  }
}

private fun <E> List<E>.sumBy(selector: (E) -> Boolean): Int {
  return 1
}