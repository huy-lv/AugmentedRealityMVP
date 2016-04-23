
APP_MODULES		:= nftSimpleNative
#APP_OPTIM		:= release
#APP_OPTIM		:= debug
#APP_ABI			:= all
APP_ABI			:= armeabi armeabi-v7a x86 mips
#APP_ABI			:= armeabi armeabi-v7a x86 mips arm64-v8a x86_64 mips64
APP_PLATFORM    := android-15
APP_STL 		:= c++_shared
APP_CPPFLAGS 	:= -frtti -fexceptions
NDK_TOOLCHAIN_VERSION := clang
