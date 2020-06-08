// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['-Batman Quote', '-Superman Quote', '-Starwars Quote', '-Avengers Quote'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;

}

// Transition delay in miliseconds
const delay = 4000;

// List of image paths
const imgPathArr = ["image1.jpg", "image2.JPG", "image3.JPG", "image4.jpg", 
  "image5.png", "image6.jpg", "image7.png", "image8.png", "image9.png", 
  "image10.png"];

// List of texts associated with every image
const imgTextArr = ["Carnegie Mellon Hamerschlag Hall, Fall 2018", 
                    "Los Angeles, Spring 2018", 
                    "Aerial view of Pittsburgh from Cathedral of Learning, Fall 2018", 
                    "Google Mountain View, Summer 2019", 
                    "San Francisco, Summer 2019", 
                    "Google Kirkland, Summer 2019", 
                    "Downtown Seattle seen from Space Needle, Summer 2019", 
                    "Wallace Falls State Park, Summer 2019", 
                    "Downtown Vancouver seen from water plane, Summer 2019", 
                    "Downtown Seattle seen from Kerry Park, Summer 2019"];

// Transitions through images with the onload event
window.onload = function() {
    var image = document.getElementById("imgID");
    var textBox = document.getElementById("imgText");
    // accounts for the first iteration that sets index to 0
    var index = -1;
    function transition() {
        index = (index + 1) % imgPathArr.length;
        image.src = 'images/' + imgPathArr[index];
        textBox.innerHTML = imgTextArr[index];
    }
    setInterval(transition, delay);
}
