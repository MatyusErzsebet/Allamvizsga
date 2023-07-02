package com.example.furnitureapp.fragments

import android.net.Uri
import android.opengl.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.example.furnitureapp.utils.Constants
import com.google.ar.core.Config
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment


class CustomArFragment: ArFragment() {
    private lateinit var modelRenderable: ModelRenderable
    private var glbPath: String? = null
    private var glbScale: Float? = null
    override fun getSessionConfiguration(session: Session): Config {

        val config = Config(session)
        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE


        this.arSceneView.setupSession(session)


        return config
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glbPath = requireArguments().getString(Constants.GLBPATH)
        glbScale = requireArguments().getFloat(Constants.GLBSCALE)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arSceneView.planeRenderer.isEnabled = true

        val modelUri = Uri.parse(glbPath)
        val renderableSource = RenderableSource.builder().setSource(
            requireContext(),
            modelUri,
            RenderableSource.SourceType.GLB
        ).build()

        ModelRenderable.builder()
            .setSource(requireContext(), renderableSource)
            .build()
            .thenAccept { renderable -> modelRenderable = renderable}

            .exceptionally { throwable ->
                // Handle any errors that occurred during loading the renderable
                return@exceptionally null
            }

        setOnTapArPlaneListener{ hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            val x = motionEvent.x
            val y = motionEvent.y

            val frame = arSceneView.arFrame
            val camera = frame?.camera

            val cameraPose = camera?.pose
            val cameraMatrix = FloatArray(16)
            cameraPose?.toMatrix(cameraMatrix, 0)

            val invertedCameraMatrix = FloatArray(16)
            Matrix.invertM(invertedCameraMatrix, 0, cameraMatrix, 0)

            val normalizedX = (x / arSceneView.width - 0.5f) * 2.0f
            val normalizedY = -(y / arSceneView.height - 0.5f) * 2.0f

            val rayOrigin = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
            val rayDirection = floatArrayOf(normalizedX, normalizedY, -1.0f, 0.0f)

            val transformedRayOrigin = FloatArray(4)
            val transformedRayDirection = FloatArray(4)

            Matrix.multiplyMV(transformedRayOrigin, 0, invertedCameraMatrix, 0, rayOrigin, 0)
            Matrix.multiplyMV(transformedRayDirection, 0, invertedCameraMatrix, 0, rayDirection, 0)

            val anchor = hitResult.createAnchor()
            var scalingFactor = 1f // Replace with your desired scaling factor
            if(glbScale != null){
                scalingFactor = glbScale!!
            }

// Create an AnchorNode and set its position
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arSceneView.scene)

// Set the renderable for the anchor node // Your ModelRenderable

            val node = Node()
            node.setParent(anchorNode)
            node.renderable = modelRenderable

// Scale the node
            node.localScale = Vector3(scalingFactor, scalingFactor, scalingFactor)

// Set the initial position of the anchor node
            node.localPosition = Vector3(0f, 0f, 0f)

        }



    }

    override fun onPause() {
        super.onPause()
        this.arSceneView.pause()
    }

    override fun onResume() {
        super.onResume()
        try {
            this.arSceneView.resume()
        } catch (e: CameraNotAvailableException) {
            e.printStackTrace()
        }
    }
}