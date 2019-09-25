var gulp = require('gulp');
var gulpcli = require('gulp-cli');
var cleanCSS = require('gulp-clean-css');
var rename = require("gulp-rename");
var concat = require('gulp-concat');
var pkg = require('../../package.json');
var sass = require('gulp-sass');

sass.compiler = require('node-sass');

// Compile SASS
gulp.task('sass', function (done) {
  return gulp.src('./sass/**/*.scss')
    .pipe(sass().on('error', sass.logError))
    .pipe(gulp.dest('./css'));
  done();
});

// Minify compiled CSS
gulp.task('minify-css', function(done) {
  return gulp.src(['../../node_modules/font-awesome/css/font-awesome.css',
                   '../../node_modules/bulma/css/bulma.min.css',
                   'css/*.css'
                  ])
    .pipe(concat('boodle.css'))
    .pipe(cleanCSS())
    .pipe(rename({ suffix: '.min' }))
    .pipe(gulp.dest('../public/css'));
  done();
});

// Copy vendor libraries from /node_modules into /vendor
gulp.task('copy', function(done) {
  gulp.src(['../../node_modules/font-awesome/fonts/*'])
    .pipe(gulp.dest('../public/fonts'));
  done();
});

// Run everything
gulp.task('default', gulp.series(['sass', 'minify-css', 'copy']));
