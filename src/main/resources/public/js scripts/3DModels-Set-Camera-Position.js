import * as THREE from 'three';
import { showroom, scene } from "../js scripts/3DModel-Scene-Switching.js";

export function setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, controls, pivot) {
    const nearClip = 0.1;

    // Set the near and far clipping planes for the camera
    camera.near = nearClip;
    camera.fov = 30;
    camera.far = 1100;
    camera.updateProjectionMatrix();

    if (carParam.includes(`Tesla-Model-3-2020.glb`)) {
        camera.far = 15000;
        camera.updateProjectionMatrix();
    }

    if (model && centerPointPosition) {
        // Add the model as a child of the showroom
        showroom.add(model);

        // Add the camera as a child of the showroom
        showroom.add(camera);

        // Calculate bounding boxes of the model and the showroom
        const modelBoundingBox = new THREE.Box3().setFromObject(model);
        const showroomBoundingBox = new THREE.Box3().setFromObject(showroom);

        // Calculate the scaling factor required to fit the model inside the showroom
        const modelSize = modelBoundingBox.getSize(new THREE.Vector3());
        const showroomSize = showroomBoundingBox.getSize(new THREE.Vector3());
        const scale = Math.min(
            showroomSize.x / modelSize.x,
            showroomSize.y / modelSize.y,
            showroomSize.z / modelSize.z
        ) * 0.14; // Adjusting scale to make the model smaller

        // Apply scale to the model
        model.scale.set(scale, scale, scale);

        // Calculate the required position adjustments to center the model inside the showroom
        const modelCenter = new THREE.Vector3();
        modelBoundingBox.getCenter(modelCenter);
        const showroomCenter = new THREE.Vector3();
        showroomBoundingBox.getCenter(showroomCenter);

        // Adjust position to center the model inside the showroom
        model.position.sub(modelCenter.multiplyScalar(scale)); // Apply scale to modelCenter
        model.position.add(showroomCenter);

        // Adjust position to make model touch the centerPointPosition
        model.position.y = centerPointPosition.y;

        const centerPointBoundingBox = new THREE.Box3().expandByPoint(centerPointPosition);
        const intersects = modelBoundingBox.intersectsBox(centerPointBoundingBox);

        // If there is a collision, move the model up until it no longer intersects with the vector
        if (intersects) {
            // Calculate the offset needed to move the model up
            const offsetY = modelBoundingBox.max.y - centerPointPosition.y;

            // Move the model up by the calculated offset
            model.position.y -= offsetY;
        }

        // Update pivot position
        if (pivot !== undefined) {
            pivot.position.copy(centerPointPosition);
        }

        // Set camera position
        const modelDistance = Math.max(modelSize.x, modelSize.y, modelSize.z);
        const cameraOffset = new THREE.Vector3(0, 0, modelDistance * 2); // Adjust multiplier for distance
        const cameraPosition = centerPointPosition.clone().add(cameraOffset);
        camera.position.copy(cameraPosition);

        // Set camera's Y position to match the model's Y position
        camera.position.y = model.position.y;

        // Set camera orientation to look at the centerPointPosition
        camera.lookAt(centerPointPosition);

        // Set controls target
        if (controls.target !== undefined) {
            controls.target.copy(centerPointPosition);
        }
    }

    // Add lights to the scene
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(0, 1, 0).normalize();
    scene.add(directionalLight);

    const directionalLightRight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLightRight.position.set(1, 0, 0);
    scene.add(directionalLightRight);

    const directionalLightLeft = new THREE.DirectionalLight(0xffffff, 1);
    directionalLightLeft.position.set(-1, 0, 0);
    scene.add(directionalLightLeft);

    const ambientLight = new THREE.AmbientLight(0xffffff, 1);
    scene.add(ambientLight);
}
