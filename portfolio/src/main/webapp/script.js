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

// Key-Value object map of image path to associated text description
const imgPathArr = {
    "image0.png": "Downtown Seattle seen from Kerry Park, Summer 2019",
    "image1.png": "Carnegie Mellon Hamerschlag Hall, Fall 2018", 
    "image2.png": "Los Angeles, Spring 2018", 
    "image3.png": "Aerial view of Pittsburgh from Cathedral of Learning, Fall 2018", 
    "image4.png": "Google Mountain View, Summer 2019",
    "image5.png": "San Francisco, Summer 2019", 
    "image6.png": "Google Kirkland, Summer 2019",
    "image7.png": "Downtown Seattle seen from Space Needle, Summer 2019", 
    "image8.png": "Wallace Falls State Park, Summer 2019",
    "image9.png": "Downtown Vancouver seen from water plane, Summer 2019" 
};

// The index in the image path that removes 'images/' in order to match a 
// given key
const pathSliceIdx = 7;

// Transitions through images with the onload event
window.onload = function() {
    
    var image = document.getElementById("imgID");
    var textBox = document.getElementById("imgText");
    
    // accounts for the first iteration that sets index to 0
    var index = -1;
    var imgPathStr = '';
    
    function transition() {
        index = (index + 1) % Object.keys(imgPathArr).length;
        imgPathStr = 'images/' + 'image' + index.toString() + '.png';
        image.src = imgPathStr;
        textBox.innerHTML = imgPathArr[imgPathStr.slice(7)];
    }
    
    setInterval(transition, delay);
}
