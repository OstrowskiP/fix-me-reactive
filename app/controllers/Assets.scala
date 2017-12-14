package controllers

import javax.inject.Inject

import controllers.Assets.Asset
import utils.ErrorHandler

class MyAssets @Inject() (val errorHandler: ErrorHandler) extends AssetsBuilder(errorHandler) {
  def public(path: String, file: Asset) = versioned(path, file)

  def lib(path: String, file: Asset) = versioned(path, file)

  def css(path: String, file: Asset) = versioned(path, file)

  def commonCss(path: String, file: Asset) = versioned(path, file)

  def js(path: String, file: Asset) = versioned(path, file)

  def commonJs(path: String, file: Asset) = versioned(path, file)

  def img(path: String, file: Asset) = versioned(path, file)

  def commonImg(path: String, file: Asset) = versioned(path, file)
}